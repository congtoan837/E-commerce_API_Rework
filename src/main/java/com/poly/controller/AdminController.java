package com.poly.controller;

import java.util.List;
import java.util.UUID;

import com.poly.dto.request.*;
import com.poly.services.*;
import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.UserResponse;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.exception.GlobalException;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    PasswordEncoder passwordEncoder;

    UserService userService;
    RoleService roleService;
    PermissionService permissionService;
    CategoryService categoryService;
    ProductService productService;

    @GetMapping("/user/get")
    public ApiResponse<?> getAllUser(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        List<UserResponse> responses = userService.getAll(page, size);
        return GlobalException.AppResponse(responses);
    }

    @PostMapping("/user/create")
    public ApiResponse<?> createUser(@RequestBody @Valid UserRequest request) {
        if (userService.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTS);

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        return GlobalException.AppResponse(userService.create(request));
    }

    @PutMapping("/user/update")
    public ApiResponse<?> updateUser(@RequestBody @Valid UserRequest request) {
        if (StringUtils.isNotBlank(request.getPassword()))
            request.setPassword(passwordEncoder.encode(request.getPassword()));

        return GlobalException.AppResponse(userService.update(request));
    }

    @DeleteMapping("/user/delete/{userId}")
    public ApiResponse<?> deleteUser(@PathVariable @Valid UUID userId) {
        userService.delete(userId);
        return GlobalException.AppResponse(Boolean.TRUE);
    }

    @PostMapping("/permission/create")
    public ApiResponse<?> createPermission(@RequestBody @Valid PermissionRequest request) {
        return GlobalException.AppResponse(permissionService.create(request));
    }

    @GetMapping("/permission/get")
    public ApiResponse<?> getAllPermission(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return GlobalException.AppResponse(permissionService.getAll(page, size));
    }

    @DeleteMapping("/permission/delete/{permissionName}")
    public ApiResponse<?> deletePermission(@PathVariable @Valid String permissionName) {
        permissionService.delete(permissionName);
        return GlobalException.AppResponse(Boolean.TRUE);
    }

    @PostMapping("/role/create")
    public ApiResponse<?> createRole(@RequestBody @Valid RoleRequest request) {
        return GlobalException.AppResponse(roleService.create(request));
    }

    @GetMapping("/role/get")
    public ApiResponse<?> getAllRole(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return GlobalException.AppResponse(roleService.getAll(page, size));
    }

    @DeleteMapping("/role/delete/{roleName}")
    public ApiResponse<?> deleteRole(@PathVariable @Valid String roleName) {
        roleService.delete(roleName);
        return GlobalException.AppResponse(Boolean.TRUE);
    }

    @PostMapping("/category/create")
    public ApiResponse<?> createCategory(@RequestBody @Valid CategoryRequest request) {
        return GlobalException.AppResponse(categoryService.create(request));
    }

    @GetMapping("/category/get")
    public ApiResponse<?> getAllCategory(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return GlobalException.AppResponse(categoryService.getAll(page, size));
    }

    @DeleteMapping("/category/delete/{categoryName}")
    public ApiResponse<?> deleteCategory(@PathVariable @Valid String categoryName) {
        categoryService.delete(categoryName);
        return GlobalException.AppResponse(Boolean.TRUE);
    }

    @PostMapping("/product/create")
    public ApiResponse<?> createProduct(@RequestBody @Valid ProductRequest request) {
        return GlobalException.AppResponse(productService.create(request));
    }

    @GetMapping("/product/get")
    public ApiResponse<?> getAllProduct(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return GlobalException.AppResponse(productService.getAll(page, size));
    }

    @PutMapping("/product/update")
    public ApiResponse<?> updateProduct(@RequestBody @Valid ProductRequest request) {
        return GlobalException.AppResponse(productService.update(request));
    }

    @DeleteMapping("/product/delete/{productName}")
    public ApiResponse<?> deleteProduct(@PathVariable @Valid UUID productName) {
        productService.delete(productName);
        return GlobalException.AppResponse(Boolean.TRUE);
    }
}
