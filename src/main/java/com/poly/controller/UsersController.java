package com.poly.controller;

import com.poly.dto.image.*;
import com.poly.dto.product.ProductGetDto;
import com.poly.dto.user.*;
import com.poly.entity.User;
import com.poly.ex.AmazonClient;
import com.poly.ex.ModelMapperConfig;
import com.poly.ex.StringContent;
import com.poly.services.AuthService;
import com.poly.services.ResponseUtils;
import com.poly.services.UserService;
import org.apache.commons.lang3.StringUtils;
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
public class UsersController {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Autowired
    private ResponseUtils responseUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapperConfig mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(@RequestParam int page, @RequestParam int size, @RequestParam(defaultValue = "") String search) {
        try {
            Page<User> users = userService.getAllUser(search, PageRequest.of(page, size));
            Page<UserGetDto> result = mapper.mapEntityPageIntoDtoPage(users, UserGetDto.class);
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get user success!", users.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserPostDto request) {
        try {
            if (userService.getByUsername(request.getUsername()) != null)
                return responseUtils.getResponseEntity(null, "-1", "Username is already exists!", HttpStatus.BAD_REQUEST);
            if (request.getUsername().length() < 6)
                return responseUtils.getResponseEntity(null, "-1", "Username must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            if (request.getPassword().length() < 6)
                return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            if (!validate(request.getEmail()))
                return responseUtils.getResponseEntity(null, "-1", "Email is not in the correct formatting!", HttpStatus.BAD_REQUEST);
            // default
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            if (StringUtils.isBlank(request.getImage())) {
                request.setImage(StringContent.avatar_default);
            }
            // save
            User user = userService.save(mapper.map(request, User.class));
            return responseUtils.getResponseEntity(user, "1", "Create user success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserPostDto request) {
        try {
            if (request.getUsername().length() < 6)
                return responseUtils.getResponseEntity(null, "-1", "Username must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            if (request.getPassword().length() < 6)
                return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);

            User user = userService.getById(request.getId());
            if (user == null) {
                return responseUtils.getResponseEntity("-1", "User not found!", HttpStatus.BAD_REQUEST);
            }
            // default
            if (StringUtils.isNotBlank(request.getPassword()))
                request.setPassword(passwordEncoder.encode(request.getPassword()));
            // save
            userService.save(mapper.map(request, User.class));
            return responseUtils.getResponseEntity("1", "Update user success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody UserPostDto request) {
        try {
            User user = userService.getById(request.getId());
            if (user == null) {
                return responseUtils.getResponseEntity("-1", "User not found!", HttpStatus.BAD_REQUEST);
            }

            userService.deleteById(request.getId());
            return responseUtils.getResponseEntity("1", "Delete user success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }
}
