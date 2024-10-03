package com.poly.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleResponse {
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
}
