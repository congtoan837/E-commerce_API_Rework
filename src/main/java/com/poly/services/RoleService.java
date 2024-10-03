package com.poly.services;

import com.poly.dto.Request.RoleRequest;
import com.poly.dto.Response.RoleResponse;
import com.poly.entity.Permission;
import com.poly.entity.Role;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.RoleMapper;
import com.poly.repositories.PermissionRepository;
import com.poly.repositories.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse Create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions((Set<Permission>) permissions);

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return roleMapper.toRoleResponseList(roleRepository.findByIsDeletedFalse(pageable).getContent());
    }

    public void delete(String Name) {
        Role role = roleRepository.findByNameAndIsDeletedFalse(Name)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        role.setDeleted(true);

        roleRepository.save(role);
    }
}
