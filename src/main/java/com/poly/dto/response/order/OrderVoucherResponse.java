package com.poly.dto.response.order;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderVoucherResponse {
    private UUID id;
    private String code;
    private String title;
}
