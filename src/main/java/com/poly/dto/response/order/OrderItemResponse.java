package com.poly.dto.response.order;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private UUID id;
    private Product product;
    private Variant variant;
    private Long amount;
    private Long discount;
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
