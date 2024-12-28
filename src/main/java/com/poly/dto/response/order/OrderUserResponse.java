package com.poly.dto.response.order;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderUserResponse {
    private UUID id;
    private String username;
}
