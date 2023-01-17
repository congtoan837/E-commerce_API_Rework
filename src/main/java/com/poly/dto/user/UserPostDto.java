package com.poly.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserPostDto {
    private UUID id;

    private String name;

    private String image;

    private String email;

    private String address;

    private String username;

    private String password;

    private Set<Role> roles = new HashSet<>();
}
