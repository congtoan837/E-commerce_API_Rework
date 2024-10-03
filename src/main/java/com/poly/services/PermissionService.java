package com.poly.services;

import com.poly.dto.Request.PermissionRequest;
import com.poly.dto.Response.PermissionResponse;
import com.poly.entity.Permission;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.PermissionMapper;
import com.poly.repositories.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public Optional<Permission> getById(String id) {
        return permissionRepository.findById(id);
    }

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return permissionMapper.toPermissionResponseList(permissionRepository.findByIsDeletedFalse(pageable).getContent());
    }

    public void delete(String Id) {
        Permission permission = permissionRepository.findByNameAndIsDeletedFalse(Id)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        permission.setDeleted(true);

        permissionRepository.save(permission);
    }
}
