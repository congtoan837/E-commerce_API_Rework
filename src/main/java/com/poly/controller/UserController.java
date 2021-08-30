package com.poly.controller;

import com.poly.dto.ImageDto;
import com.poly.dto.UserGetDto;
import com.poly.entity.User;
import com.poly.ex.AmazonClient;
import com.poly.services.ResponseUtils;
import com.poly.services.UserService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
@Api(value = "User APIs")
public class UserController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AmazonClient amazonClient;

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy,
                                        @RequestParam String sortType, @RequestParam(defaultValue = "") String search) {
        try {
            String S = sortType.trim().toLowerCase();
            Sort.Direction dir = null;
            if (S.equals("asc")) {
                dir = Sort.Direction.ASC;
            } else if (S.equals("desc")) {
                dir = Sort.Direction.DESC;
            }

            Page<User> users = userService.getAllUser(search, PageRequest.of(page, size, Sort.by(dir, sortBy)));
            Page<Object> result = users.map(product -> mapper.map(product, UserGetDto.class));
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get user success!", users.getTotalElements(), HttpStatus.OK);

        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@ModelAttribute User user, @ModelAttribute MultipartFile file) {
        try {

            if (userService.getByUsername(user.getUsername()) != null) {
                return responseUtils.getResponseEntity(null, "-1", "Username is already exists!", HttpStatus.BAD_REQUEST);
            } else if (user.getPassword().length() < 6) {
                return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            } else if (user.getPhone().length() < 10) {
                return responseUtils.getResponseEntity(null, "-1", "Number phone must be at 11 digit!", HttpStatus.BAD_REQUEST);
            } else {
                ImageDto imgDto = amazonClient.uploadFile(file);
                user.setImage(imgDto.url);
                userService.save(user);
                return responseUtils.getResponseEntity("1", "Create user success!", HttpStatus.OK);
            }

        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Create user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {

            Long id = user.getId();
            User getUser = userService.getById(id);

            if (getUser != null) {

                getUser.setName(user.getName());
                getUser.setEmail(user.getEmail().trim());
                getUser.setAddress(user.getAddress());
                getUser.setEnabled(user.isEnabled());
                if (!user.getImage().equals("")) {
                    getUser.setImage(user.getImage());
                }
                if (user.getRoles().size() > 0) {
                    getUser.setRoles(user.getRoles());
                }

                userService.save(getUser);

                UserGetDto userMapper = mapper.map(getUser, UserGetDto.class);

                return responseUtils.getResponseEntity(userMapper, "1", "Update user success!", HttpStatus.OK);
            } else {
                return responseUtils.getResponseEntity("-1", "User " + id + " not found!", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Update user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            User getUser = userService.getById(id);

            if (getUser == null) {
                return responseUtils.getResponseEntity("-1", "User " + id + " not found!", HttpStatus.BAD_REQUEST);
            } else {
                userService.deleteById(id);
                return responseUtils.getResponseEntity("1", "Delete user success!", HttpStatus.OK);
            }

        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }
}
