package com.poly.dto.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {
    private long amount;
    private UUID orderId;
}
