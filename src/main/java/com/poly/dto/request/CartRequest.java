package com.poly.dto.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class CartRequest {
    @NotNull
    private UUID product_id;
    private UUID variant_id;
    @Min(value = 1, message = "QUANTITY_INVALID")
    private int quantity;
}
