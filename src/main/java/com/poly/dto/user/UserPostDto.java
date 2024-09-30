package com.poly.dto.user;

import lombok.Getter;
import lombok.Setter;

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

    private String Role;
}
