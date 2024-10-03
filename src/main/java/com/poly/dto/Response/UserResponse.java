package com.poly.dto.Response;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String image;
    private String username;
    private Set<Role> roles;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Role {
        private String name;
        private String description;
    }
}
