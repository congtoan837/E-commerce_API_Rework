package com.poly.controller;

import com.poly.dto.SignupRequest;
import com.poly.dto.UserPostDto;
import com.poly.entity.JwtResponse;
import com.poly.dto.LoginRequest;
import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.ex.JwtUtils;
import com.poly.services.AuthService;
import com.poly.services.ResponseUtils;
import com.poly.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            AuthService userDetails = (AuthService) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            return getResponseEntity(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles), "Login success!", HttpStatus.OK);
        } catch (Exception e) {
            return getResponseEntity(null, "Login fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
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

                Set<Role> roles = new HashSet<>();
                Role role = new Role();
                role.setId(3L);
                roles.add(role);

                user.setRoles(roles);
                if (user.getImage() == null) {
                    user.setImage("https://congtoan-bucket.s3.ap-southeast-1.amazonaws.com/1630749704358-avatar.png");
                }
                userService.save(user);

                return responseUtils.getResponseEntity("1", "Create user success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity(null, "-1", "Create user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> getResponseEntity(Object data, String mess, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("Data", data);
        response.put("Status", status);
        response.put("Messenger", mess);
        return new ResponseEntity<>(response, status);
    }
}
