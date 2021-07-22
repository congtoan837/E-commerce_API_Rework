package com.poly.controller;

import com.poly.entity.*;

import com.poly.services.ResponseUtils;
import com.poly.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    UserService userService;

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(@RequestParam int page, Authentication authentication) {
        try {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                Page<User> users = userService.findAll(PageRequest.of(page, 5, Sort.by("role").ascending()));
                return responseUtils.getResponseEntity(users, "1", "Get user success!", HttpStatus.OK);
            }else {
                return responseUtils.getResponseEntity("-1", "you not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return responseUtils.getResponseEntity(null, "-1", "Get user fail!", HttpStatus.OK);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user, Authentication authentication) {
        try {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                if (userService.getByUser(user.getUsername()) != null) {
                    return responseUtils.getResponseEntity(null, "-1", "Username is already exists!", HttpStatus.BAD_REQUEST);
                } else if (user.getPassword().length() < 6) {
                    return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
                } else if (user.getPhone().length() < 10) {
                    return responseUtils.getResponseEntity(null, "-1", "Number phone must be at 11 digit!", HttpStatus.BAD_REQUEST);
                } else {
                    User usersList = userService.save(user);
                    return responseUtils.getResponseEntity(usersList, "1", "Create user success!", HttpStatus.OK);
                }
            }else {
                return responseUtils.getResponseEntity("-1", "you not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return responseUtils.getResponseEntity(null, "-1", "Create user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user, Authentication authentication) {
        try {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                Integer id = user.getId();
                Optional<User> getUser = userService.findById(id).map(myUser -> {
                    myUser.setName(user.getName());
                    myUser.setEmail(user.getEmail());
                    myUser.setPhone(user.getPhone());
                    myUser.setPassword(user.getPassword());
                    myUser.setAddress(user.getAddress());
                    myUser.setStatus(user.getStatus());
                    myUser.setUsername(user.getUsername());
                    myUser.setRole(user.getRole());
                    if (user.getImage().equals("")) {
                        myUser.setImage(user.getImage());
                    }
                    return userService.save(myUser);
                });
                if (getUser.isEmpty()) {
                    return responseUtils.getResponseEntity(null, "-1", "Update user fail!", HttpStatus.BAD_REQUEST);
                }
                return responseUtils.getResponseEntity(getUser, "1", "Update user success!", HttpStatus.OK);
            }else {
                return responseUtils.getResponseEntity("-1", "you not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            return responseUtils.getResponseEntity(null, "-1", "Update user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id, Authentication authentication) {
        try {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                userService.deleteById(id);
                return responseUtils.getResponseEntity("1", "Delete user success!", HttpStatus.BAD_REQUEST);
            }else {
                return responseUtils.getResponseEntity("-1", "you not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }
}
