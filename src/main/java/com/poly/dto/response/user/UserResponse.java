package com.poly.dto.response.user;

import java.util.Set;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String image;
    private Set<RoleResponse> roles;
}
