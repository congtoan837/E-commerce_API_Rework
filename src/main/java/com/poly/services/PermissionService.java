package com.poly.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.dto.request.PermissionRequest;
import com.poly.dto.response.PermissionResponse;
import com.poly.entity.Permission;
import com.poly.mapper.PermissionMapper;
import com.poly.repositories.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getAll() {
        return permissionMapper.toPermissionResponseList(permissionRepository.findAll());
    }

    public void delete(String id) {
        permissionRepository.deleteById(id);
    }
}
