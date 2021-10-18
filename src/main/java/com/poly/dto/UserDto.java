package com.poly.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    private String name;

    private String image;

    private String username;
}
