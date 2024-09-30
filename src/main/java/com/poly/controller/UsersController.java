package com.poly.controller;

import com.poly.dto.user.*;
import com.poly.entity.User;
import com.poly.ex.ModelMapperConfig;
import com.poly.ex.StringContent;
import com.poly.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UsersController {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
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
    public ResponseEntity<?> getAllUser(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "1") int size, @RequestParam(defaultValue = "") String search) {
        try {
            Page<User> users = userService.getAllUser(PageRequest.of(page, size));
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserPostDto request) {
        try {
            if (request.getUsername().length() < 6)
                return new ResponseEntity<>("Username must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            if (request.getPassword().length() < 6)
                return new ResponseEntity<>("Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            if (!validate(request.getEmail()))
                return new ResponseEntity<>("Email is not in the correct formatting!", HttpStatus.BAD_REQUEST);

            if (userService.getByUsername(request.getUsername()) != null)
                return new ResponseEntity<>("Username is already exists!", HttpStatus.BAD_REQUEST);
            // encode password
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            // set default avatar if not input
            if (StringUtils.isBlank(request.getImage()))
                request.setImage(StringContent.avatar_default);
            userService.save(mapper.map(request, User.class));
            return new ResponseEntity<>(StringContent.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserPostDto request) {
        try {
            if (request.getPassword().length() < 6)
                return new ResponseEntity<>("Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            if (!validate(request.getEmail()))
                return new ResponseEntity<>("Email is not in the correct formatting!", HttpStatus.BAD_REQUEST);

            User user = userService.getById(request.getId());
            if (user == null)
                return new ResponseEntity<>(StringContent.NOT_FOUND, HttpStatus.BAD_REQUEST);
            // default
            if (StringUtils.isNotBlank(request.getPassword()))
                request.setPassword(passwordEncoder.encode(request.getPassword()));
            // save
            userService.save(mapper.map(request, User.class));
            return new ResponseEntity<>(StringContent.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody UserPostDto request) {
        try {
            User user = userService.getById(request.getId());
            if (user == null) {
                return new ResponseEntity<>(StringContent.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }

            userService.deleteById(request.getId());
            return new ResponseEntity<>(StringContent.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }
}
