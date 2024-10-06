package com.poly.services;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.poly.dto.request.RoleRequest;
import com.poly.dto.response.RoleResponse;
import com.poly.entity.Permission;
import com.poly.entity.Role;
import com.poly.mapper.RoleMapper;
import com.poly.repositories.PermissionRepository;
import com.poly.repositories.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(Set.copyOf(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return roleMapper.toRoleResponseList(roleRepository.findAll(pageable).getContent());
    }

    public void delete(String Name) {
        roleRepository.deleteById(Name);
    }
}
