package com.poly.dto.response.cart;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponse {
    private Set<CartItemsResponse> cartItems;
    private Long totalPrice;
}
