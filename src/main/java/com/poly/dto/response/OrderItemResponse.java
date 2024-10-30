package com.poly.dto.response;

import java.util.UUID;

import lombok.Data;

@Data
public class OrderItemResponse {
    private UUID id;
    private ProductResponse product;
    private int quantity;
}
