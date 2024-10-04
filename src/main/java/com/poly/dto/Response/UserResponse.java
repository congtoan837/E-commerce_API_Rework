package com.poly.dto.Response;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String image;
    private String username;
    private Set<RoleResponse> roles;
}
