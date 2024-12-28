package com.poly.dto.response.order;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderVoucherResponse {
    private UUID id;
    private String code;
    private String title;
}
