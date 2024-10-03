package com.poly.mapper;

import com.poly.dto.Request.PermissionRequest;
import com.poly.dto.Response.PermissionResponse;
import com.poly.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toPermissionResponseList(List<Permission> permissions);

    void updatePermissionFromPermissionRequest(@MappingTarget Permission permission, PermissionRequest request);
}
