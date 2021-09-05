package com.poly.dto;

import com.poly.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class SignupRequest {
    private String name;

    private String email;

    private String phone;

    private String address;

    private String username;

    private String password;
}
