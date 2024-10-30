package com.poly.dto.response;

import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class CartResponse {
    private UUID id;
    private Set<CartItemsResponse> cartItems;
}
