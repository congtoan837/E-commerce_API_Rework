package com.poly.dto.response;

import java.util.UUID;

import lombok.Data;

@Data
public class OrderResponse {
    private UUID id;
    private String orderName;
    private String orderPhone;
    private String orderAddress;
    private String voucherCode;
    private Long originalAmount;
    private Long discountAmount;
    private Long totalAmount;
    private String status;
    private String note;

    private OrderItemResponse orderItem;
    private UserResponse user;
}
