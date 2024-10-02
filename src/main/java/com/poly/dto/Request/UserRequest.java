package com.poly.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserRequest {
    private UUID id;

    private String name;
    private String image;
    private String email;
    private String address;
    private String username;
    private String password;
}
