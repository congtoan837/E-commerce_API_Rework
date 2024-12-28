package com.poly.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.RoleRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.user.RoleResponse;
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
}
