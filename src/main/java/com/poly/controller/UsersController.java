package com.poly.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.poly.dto.Response.ApiResponse;
import com.poly.entity.User;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.exception.GlobalException;
import com.poly.mapper.UserMapper;
import com.poly.services.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersController {
    UserService userService;

    UserMapper userMapper;

    @GetMapping("/info")
    public ApiResponse<?> getInfoUser(Authentication authentication) {
        String userId = ((Jwt) authentication.getPrincipal()).getClaimAsString("userId");

        User user = userService.getById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return GlobalException.AppResponse(userMapper.toUserResponse(user));
    }
}
