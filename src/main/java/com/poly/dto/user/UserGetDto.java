package com.poly.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserGetDto {
    private UUID id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String image;

    private boolean enabled;

    private String username;

    private Set<Role> roles;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime modifiedLastTime;
}
