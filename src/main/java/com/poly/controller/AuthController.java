package com.poly.controller;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poly.config.ModelMapperConfig;
import com.poly.dto.request.LoginRequest;
import com.poly.dto.request.UserRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.JwtResponse;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.exception.GlobalException;
import com.poly.services.AuthService;
import com.poly.services.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    PasswordEncoder passwordEncoder;
    AuthService authService;
    UserService userService;
    ModelMapperConfig mapper;

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @PostMapping("/login")
    public ApiResponse<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        JwtResponse response = authService.authenticate(loginRequest);
        return GlobalException.appResponse(response);
    }

    @PostMapping("/refreshToken")
    public ApiResponse<?> refreshToken(@RequestBody @Valid Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        JwtResponse response = authService.refreshToken(refreshToken);
        return GlobalException.appResponse(response);
    }

    @PostMapping("/signup")
    public ApiResponse<?> signup(@RequestBody @Valid UserRequest request) {
        if (userService.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USERNAME_EXISTS);

        return GlobalException.appResponse(userService.create(request));
    }

    @GetMapping("/infoUser")
    public ApiResponse<?> getInfo(Authentication authentication) {
        String userId = ((Jwt) authentication.getPrincipal()).getClaimAsString("userId");

        return GlobalException.appResponse(userService.getInfo(userId));
    }
}
