package com.poly.controller;

import com.poly.dto.ImageDto;
import com.poly.dto.UserGetDto;
import com.poly.dto.UserPostDto;
import com.poly.entity.User;
import com.poly.ex.AmazonClient;
import com.poly.services.AuthService;
import com.poly.services.ResponseUtils;
import com.poly.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    UserService userService;


    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy,
                                        @RequestParam String sortType, @RequestParam(defaultValue = "") String search) {
        try {
            String S = sortType.trim().toLowerCase();
            Page<User> users = userService.getAllUser(search, PageRequest.of(page, size, Sort.by(S.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)));
            Page<Object> result = users.map(user -> mapper.map(user, UserGetDto.class));
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get user success!", users.getTotalElements(), HttpStatus.OK);

        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserPostDto request) {
        try {
            if (userService.getByUsername(request.getUsername()) != null) {
                return responseUtils.getResponseEntity(null, "-1", "Username is already exists!", HttpStatus.BAD_REQUEST);
            } else if (request.getUsername().length() < 6) {
                return responseUtils.getResponseEntity(null, "-1", "Username must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            } else if (request.getPassword().length() < 6) {
                return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            } else if (!request.getPhone().matches("[0-9]+") || request.getPhone().length() != 10) {
                return responseUtils.getResponseEntity(null, "-1", "Phone number is not in the correct formatting!", HttpStatus.BAD_REQUEST);
            } else {
                User user = mapper.map(request, User.class);
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                if (user.getImage() == null) {
                    user.setImage("https://congtoan-bucket.s3.ap-southeast-1.amazonaws.com/1630749704358-avatar.png");
                }
                userService.save(user);

                return responseUtils.getResponseEntity("1", "Create user success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Create user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserPostDto request) {
        try {
            UUID id = request.getId();
            User getUser = userService.getById(id);

            if (getUser != null) {
                if (request.getUsername().length() < 6) {
                    return responseUtils.getResponseEntity(null, "-1", "Username must be at least 6 characters!", HttpStatus.BAD_REQUEST);
                } else if (request.getPassword().length() < 6) {
                    return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
                } else if (!request.getPhone().matches("[0-9]+") || request.getPhone().length() != 10) {
                    return responseUtils.getResponseEntity(null, "-1", "Phone number is not in the correct formatting!", HttpStatus.BAD_REQUEST);
                } else {
                    getUser.setName(request.getName());
                    getUser.setEmail(request.getEmail().trim());
                    getUser.setAddress(request.getAddress());
                    userService.save(getUser);

                    UserGetDto userMapper = mapper.map(getUser, UserGetDto.class);
                    return responseUtils.getResponseEntity(userMapper, "1", "Update user success!", HttpStatus.OK);
                }
            } else {
                return responseUtils.getResponseEntity("-1", "User not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Update user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            User getUser = userService.getById(id);

            if (getUser == null) {
                return responseUtils.getResponseEntity("-1", "User not found!", HttpStatus.BAD_REQUEST);
            } else {
                userService.deleteById(id);
                return responseUtils.getResponseEntity("1", "Delete user success!", HttpStatus.OK);
            }

        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity<?> deleteUser(@ModelAttribute MultipartFile file, Authentication authentication) {
        try {
            AuthService auth = (AuthService) authentication.getPrincipal();
            User user = userService.getById(auth.getId());

            ImageDto imgDto = amazonClient.uploadFile(file);
            user.setImage(imgDto.url);
            return responseUtils.getResponseEntity("1", "Upload success!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }
}
