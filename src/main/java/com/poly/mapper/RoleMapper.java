package com.poly.mapper;

import com.poly.dto.Request.RoleRequest;
import com.poly.dto.Response.RoleResponse;
import com.poly.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponseList(List<Role> roles);

//    void updateUserFromUserRequest(@MappingTarget User user, UserRequest request);
}
