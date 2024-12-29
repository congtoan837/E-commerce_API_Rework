package com.poly.dto.response.cart;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemsResponse {
    private String id;
    private Product product;
    private Variant variant;
    private int quantity;

    @Getter
    @Setter
    public static class Product {
        private UUID id;
        private String name;
    }

    @Getter
    @Setter
    public static class Variant {
        private UUID id;
        private String color;
        private String size;
        private Long price;
    }
}
