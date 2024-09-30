package com.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String name;

    private String email;

    private String address;

    private String username;

    private String password;
}
