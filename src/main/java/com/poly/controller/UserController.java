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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Autowired
    private ResponseUtils responseUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AmazonClient amazonClient;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy,
                                        @RequestParam String sortType, @RequestParam(defaultValue = "") String search) {
        try {
            String S = sortType.trim().toLowerCase();
            Page<User> users = userService.getAllUser(search, PageRequest.of(page, size, Sort.by(S.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
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
            } else if (!validate(request.getEmail())) {
                return responseUtils.getResponseEntity(null, "-1", "Email is not in the correct formatting!", HttpStatus.BAD_REQUEST);
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
            if (request.getUsername().length() < 6) {
                return responseUtils.getResponseEntity(null, "-1", "Username must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            } else if (request.getPassword().length() < 6) {
                return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            } else {
                UUID id = request.getId();
                User user = userService.getById(id);

                if (user != null) {
                    user.setName(request.getName());
                    user.setAddress(request.getAddress());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    if (request.getRoles().size() > 0) {
                        user.setRoles(request.getRoles());
                    }
                    userService.save(user);
                    return responseUtils.getResponseEntity("1", "Update user success!", HttpStatus.OK);
                } else {
                    return responseUtils.getResponseEntity("-1", "User not found!", HttpStatus.BAD_REQUEST);
                }
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

    @PutMapping("/uploadAvatar")
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
