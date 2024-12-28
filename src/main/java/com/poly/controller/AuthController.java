package com.poly.controller;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.request.LoginRequest;
import com.poly.dto.request.UserRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.JwtResponse;
import com.poly.dto.response.user.UserResponse;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.services.AuthService;
import com.poly.services.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    PasswordEncoder passwordEncoder;
    AuthService authService;
    UserService userService;

    @PostMapping("/login")
    public ApiResponse<JwtResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        return ApiResponse.<JwtResponse>builder()
                .result(authService.authenticate(loginRequest))
                .build();
    }

    @PostMapping("/refreshToken")
    public ApiResponse<JwtResponse> refreshToken(@RequestBody @Valid Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        return ApiResponse.<JwtResponse>builder()
                .result(authService.refreshToken(refreshToken))
                .build();
    }

    @PostMapping("/signup")
    public ApiResponse<UserResponse> signup(@RequestBody @Valid UserRequest request) {
        if (userService.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USERNAME_EXISTS);

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        return ApiResponse.<UserResponse>builder()
                .result(userService.create(request))
                .build();
    }

    @GetMapping("/infoUser")
    public ApiResponse<UserResponse> getInfo() {
        return ApiResponse.<UserResponse>builder().result(userService.getInfo()).build();
    }
}
