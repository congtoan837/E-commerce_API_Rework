package com.poly.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    @NotNull
    private UUID product_id;

    private UUID variant_id;

    @Min(value = 1, message = "QUANTITY_INVALID")
    private int quantity;
}
