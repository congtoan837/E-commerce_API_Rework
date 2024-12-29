package com.poly.dto.response.product;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantResponse {
    private UUID id;
    private String color;
    private String size;
    private Long price;
    private int stockQuantity;
    private int soldQuantity;
}
