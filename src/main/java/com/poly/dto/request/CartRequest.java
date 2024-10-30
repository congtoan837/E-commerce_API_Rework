package com.poly.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class CartRequest {
    private UUID product_id;
    private int quantity;
}
