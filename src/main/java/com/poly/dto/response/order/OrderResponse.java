package com.poly.dto.response.order;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    private Set<OrderItemResponse> orderItems;
    private OrderUserResponse user;
    private OrderVoucherResponse voucher;

    private Long originalAmount;
    private Long discountAmount;
    private Long totalAmount;

    private String status;

    private String note;
    private String paymentUrl;
}
