package com.poly.dto.response.product;

import lombok.Data;

import java.util.UUID;

@Data
public class VariantResponse {
    private UUID id;
    private String color;
    private String size;
    private Long price;
    private int stockQuantity;
    private int soldQuantity;
}
