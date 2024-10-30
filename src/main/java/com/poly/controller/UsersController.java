package com.poly.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.UserRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.UserResponse;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.services.UserService;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersController {
    PasswordEncoder passwordEncoder;
    UserService userService;

    @GetMapping("/get")
    public ApiResponse<?> getUser(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "") String keyword) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .result(userService.getAll(keyword, page, size))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        if (userService.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USERNAME_EXISTS);

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        return ApiResponse.<UserResponse>builder()
                .result(userService.create(request))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserRequest request) {
        if (StringUtils.isNotBlank(request.getPassword()))
            request.setPassword(passwordEncoder.encode(request.getPassword()));

        return ApiResponse.<UserResponse>builder()
                .result(userService.update(request))
                .build();
    }

    @DeleteMapping("/delete/{userId}")
    public ApiResponse<Boolean> deleteUser(@PathVariable UUID userId) {
        return ApiResponse.<Boolean>builder().result(userService.delete(userId)).build();
    }
}
