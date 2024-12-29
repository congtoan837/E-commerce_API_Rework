package com.poly.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private UUID id;
    private String note;
    private String voucherCode; // Mã voucher

    @NotBlank
    private String paymentMethod;
}
