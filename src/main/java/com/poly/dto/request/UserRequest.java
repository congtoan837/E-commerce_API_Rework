package com.poly.dto.request;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
    private UUID id;

    private String name;

    private String image;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "EMAIL_INVALID")
    private String email;

    private String address;

    @Size(min = 6, message = "USERNAME_SHORT")
    private String username;

    @Size(min = 6, message = "PASSWORD_SHORT")
    private String password;

    @NotNull(message = "ROLES_INVALID")
    private Set<String> roles;
}
