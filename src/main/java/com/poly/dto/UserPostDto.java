package com.poly.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserPostDto {
    private UUID id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String username;

    private String password;

    private Set<Role> roles;
}
