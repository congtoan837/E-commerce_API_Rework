package com.poly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.*;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.UserResponse;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.exception.GlobalException;
import com.poly.services.*;

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
    public ApiResponse<?> getUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String keyword) {
        List<UserResponse> responses = userService.getAll(keyword, page, size);
        return GlobalException.appResponse(responses);
    }

    @GetMapping("/user/count")
    public ApiResponse<?> countUser(@RequestParam(defaultValue = "") String keyword) {
        long counted = userService.countAll(keyword);

        HashMap<String, Object> response = new HashMap<>();
        response.put("numberUser", counted);
        return GlobalException.appResponse(response);
    }

    @PostMapping("/user/create")
    public ApiResponse<?> createUser(@RequestBody @Valid UserRequest request) {
        if (userService.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USERNAME_EXISTS);

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        return GlobalException.appResponse(userService.create(request));
    }

    @PutMapping("/user/update")
    public ApiResponse<?> updateUser(@RequestBody @Valid UserRequest request) {
        if (StringUtils.isNotBlank(request.getPassword()))
            request.setPassword(passwordEncoder.encode(request.getPassword()));

        return GlobalException.appResponse(userService.update(request));
    }

    @DeleteMapping("/user/delete/{userId}")
    public ApiResponse<?> deleteUser(@PathVariable @Valid UUID userId) {
        userService.delete(userId);
        return GlobalException.appResponse(Boolean.TRUE);
    }

    @PostMapping("/permission/create")
    public ApiResponse<?> createPermission(@RequestBody @Valid PermissionRequest request) {
        return GlobalException.appResponse(permissionService.create(request));
    }

    @GetMapping("/permission/get")
    public ApiResponse<?> getAllPermission(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return GlobalException.appResponse(permissionService.getAll(page, size));
    }

    @DeleteMapping("/permission/delete/{permissionName}")
    public ApiResponse<?> deletePermission(@PathVariable @Valid String permissionName) {
        permissionService.delete(permissionName);
        return GlobalException.appResponse(Boolean.TRUE);
    }

    @PostMapping("/role/create")
    public ApiResponse<?> createRole(@RequestBody @Valid RoleRequest request) {
        return GlobalException.appResponse(roleService.create(request));
    }

    @GetMapping("/role/get")
    public ApiResponse<?> getAllRole(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return GlobalException.appResponse(roleService.getAll(page, size));
    }

    @DeleteMapping("/role/delete/{roleName}")
    public ApiResponse<?> deleteRole(@PathVariable @Valid String roleName) {
        roleService.delete(roleName);
        return GlobalException.appResponse(Boolean.TRUE);
    }

    @PostMapping("/category/create")
    public ApiResponse<?> createCategory(@RequestBody @Valid CategoryRequest request) {
        return GlobalException.appResponse(categoryService.create(request));
    }

    @GetMapping("/category/get")
    public ApiResponse<?> getAllCategory(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return GlobalException.appResponse(categoryService.getAll(page, size));
    }

    @DeleteMapping("/category/delete/{categoryName}")
    public ApiResponse<?> deleteCategory(@PathVariable @Valid String categoryName) {
        categoryService.delete(categoryName);
        return GlobalException.appResponse(Boolean.TRUE);
    }

    @PostMapping("/product/create")
    public ApiResponse<?> createProduct(@RequestBody @Valid ProductRequest request) {
        return GlobalException.appResponse(productService.create(request));
    }

    @GetMapping("/product/get")
    public ApiResponse<?> getAllProduct(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return GlobalException.appResponse(productService.getAll(page, size));
    }

    @PutMapping("/product/update")
    public ApiResponse<?> updateProduct(@RequestBody @Valid ProductRequest request) {
        return GlobalException.appResponse(productService.update(request));
    }

    @DeleteMapping("/product/delete/{productName}")
    public ApiResponse<?> deleteProduct(@PathVariable @Valid UUID productName) {
        productService.delete(productName);
        return GlobalException.appResponse(Boolean.TRUE);
    }
}
