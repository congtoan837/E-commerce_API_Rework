package com.poly.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "INPUT_VALID")
    private String username;

    @NotBlank(message = "INPUT_VALID")
    private String password;
}
