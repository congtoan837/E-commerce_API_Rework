package com.poly.dto.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantRequest {
    private UUID id;

    private String color;

    private String size;

    private int price;

    private Integer stockQuantity;
}
