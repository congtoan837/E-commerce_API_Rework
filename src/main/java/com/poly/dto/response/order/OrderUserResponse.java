package com.poly.dto.response.order;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUserResponse {
    private UUID id;
    private String username;
}
