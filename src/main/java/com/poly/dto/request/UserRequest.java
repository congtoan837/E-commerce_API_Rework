package com.poly.dto.request;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.poly.validator.ValidPassword;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
    private UUID id;

    @Size(min = 6, message = "INPUT_VALID")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "INPUT_VALID")
    private String name;

    private String image;

    //    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "EMAIL_INVALID")
    private String email;

    @Pattern(regexp = "^[0-9]*$", message = "INPUT_VALID")
    private String phone;

    private String address;

    @Size(min = 6, message = "USERNAME_SHORT")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "INPUT_VALID")
    private String username;

    @ValidPassword
    @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*(),.?\":{}|<>]*$", message = "INPUT_VALID")
    private String password;

    @NotNull(message = "ROLES_INVALID")
    private Set<String> roles;
}
