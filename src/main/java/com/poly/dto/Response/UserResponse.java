package com.poly.dto.Response;

import java.util.Set;

import lombok.*;

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
