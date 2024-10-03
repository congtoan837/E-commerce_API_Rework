package com.poly.controller;

import com.poly.dto.Request.PermissionRequest;
import com.poly.dto.Request.UserRequest;
import com.poly.dto.Response.ApiResponse;
import com.poly.dto.Response.UserResponse;
import com.poly.entity.Permission;
import com.poly.entity.User;
import com.poly.ex.StringContent;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.exception.GlobalException;
import com.poly.mapper.PermissionMapper;
import com.poly.mapper.RoleMapper;
import com.poly.mapper.UserMapper;
import com.poly.services.PermissionService;
import com.poly.services.RoleService;
import com.poly.services.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    PasswordEncoder passwordEncoder;

    UserService userService;
    RoleService roleService;
    PermissionService permissionService;

    UserMapper userMapper;
    PermissionMapper permissionMapper;
    RoleMapper roleMapper;

    private static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @GetMapping("/user/get")
    public ApiResponse getAllUser(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        List<UserResponse> responses = userService.getAll(page, size);
        return GlobalException.AppResponse(responses);
    }

    @PostMapping("/user/create")
    public ApiResponse<?> createUser(@RequestBody UserRequest request) {
        if (request.getUsername().length() < 6)
            throw new AppException(ErrorCode.USERNAME_SHORT);
        if (request.getPassword().length() < 6)
            throw new AppException(ErrorCode.PASSWORD_SHORT);
        if (!validate(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_REGEX);

        if (userService.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTS);
        // encode password
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        // set default avatar if not input
        request.setImage(StringUtils.isNotBlank(request.getImage()) ? request.getImage() : StringContent.avatar_default);

        return GlobalException.AppResponse(userService.create(request));
    }

    @PutMapping("/user/update")
    public ApiResponse<?> updateUser(@RequestBody UserRequest request) {
        if (StringUtils.isNotBlank(request.getPassword()) || StringUtils.isNotBlank(request.getEmail())) {
            if (request.getPassword().length() < 6)
                throw new AppException(ErrorCode.PASSWORD_SHORT);
            if (!validate(request.getEmail()))
                throw new AppException(ErrorCode.EMAIL_REGEX);
        }
        if (StringUtils.isNotBlank(request.getPassword()))
            request.setPassword(passwordEncoder.encode(request.getPassword()));

        User user = userService.getById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // map những field không null từ request
        userMapper.updateUserFromUserRequest(user, request);

        return GlobalException.AppResponse(userService.create(request));
    }

    @DeleteMapping("/user/delete/{userId}")
    public ApiResponse<?> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return GlobalException.AppResponse(Boolean.TRUE);
    }

    @PostMapping("/permission/create")
    public ApiResponse<?> createPermission(@RequestBody PermissionRequest request) {
        return GlobalException.AppResponse(permissionService.create(request));
    }

    @GetMapping("/permission/get")
    public ApiResponse<?> getAllPermission(int page, int size) {
        return GlobalException.AppResponse(permissionService.getAll(page, size));
    }

    @PutMapping("/permission/update")
    public ApiResponse<?> updatePermission(@RequestBody PermissionRequest request) {
        Permission permission = permissionService.getById(request.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        // map những field không null từ request
        permissionMapper.updatePermissionFromPermissionRequest(permission, request);

        return GlobalException.AppResponse(permissionService.create(request));
    }

    @DeleteMapping("/permission/delete/{permissionName}")
    public ApiResponse<?> deletePermission(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return GlobalException.AppResponse(Boolean.TRUE);
    }
}
