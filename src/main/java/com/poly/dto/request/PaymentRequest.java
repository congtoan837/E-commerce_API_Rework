package com.poly.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class PaymentRequest {
    private long amount;
    private UUID orderId;
}
