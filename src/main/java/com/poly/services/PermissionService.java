package com.poly.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<PermissionResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return permissionMapper.toPermissionResponseList(
                permissionRepository.findAll(pageable).getContent());
    }

    public void delete(String Id) {
        permissionRepository.deleteById(Id);
    }
}
