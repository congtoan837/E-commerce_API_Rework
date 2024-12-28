package com.poly.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class VariantRequest {
    private UUID id;

    private String color;

    private String size;

    private int price;

    private Integer stockQuantity;
}
