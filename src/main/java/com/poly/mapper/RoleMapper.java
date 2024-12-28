package com.poly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.poly.dto.request.RoleRequest;
import com.poly.dto.response.user.RoleResponse;
import com.poly.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponseList(List<Role> roles);

    @Mapping(target = "permissions", ignore = true)
    void updateRoleFromRoleRequest(@MappingTarget Role role, RoleRequest request);
}
