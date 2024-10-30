package com.poly.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.PermissionRequest;
import com.poly.dto.request.RoleRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.PermissionResponse;
import com.poly.dto.response.RoleResponse;
import com.poly.services.PermissionService;
import com.poly.services.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;
    PermissionService permissionService;

    @PostMapping("/create")
    public ApiResponse<?> createRole(@RequestBody @Valid RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping("/get")
    public ApiResponse<?> getAllRole() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/delete/{roleName}")
    public ApiResponse<Boolean> deleteRole(@PathVariable String roleName) {
        roleService.delete(roleName);
        return ApiResponse.<Boolean>builder().result(Boolean.TRUE).build();
    }

    @PostMapping("/permission/create")
    public ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping("/permission/get")
    public ApiResponse<List<PermissionResponse>> getAllPermission() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/permission/delete/{permissionName}")
    public ApiResponse<Boolean> deletePermission(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return ApiResponse.<Boolean>builder().result(Boolean.TRUE).build();
    }
}
