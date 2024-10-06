package com.poly.dto.response;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponse {
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
}
