package com.poly.controller;

import com.poly.entity.JwtResponse;
import com.poly.entity.LoginRequest;
import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.ex.ERole;
import com.poly.ex.JwtUtils;
import com.poly.services.AuthService;
import com.poly.services.ResponseUtils;
import com.poly.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> signup(@RequestBody User user) {

        try {
            if (userService.getByUsername(user.getUsername()) != null) {
                return responseUtils.getResponseEntity(null, "-1", "Username is already exists!", HttpStatus.BAD_REQUEST);
            } else if (user.getPassword().length() < 6) {
                return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            } else if (user.getPhone().length() < 10) {
                return responseUtils.getResponseEntity(null, "-1", "Number phone must be at 11 digit!", HttpStatus.BAD_REQUEST);
            } else {
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
