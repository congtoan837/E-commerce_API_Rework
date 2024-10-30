package com.poly.dto.request;

import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderRequest {
    private UUID id;
    private String orderName;
    private String orderPhone;
    private String orderAddress;
    private String note;
    private Set<UUID> productIds; // Danh sách ID sản phẩm cần thanh toán
    private String voucherCode; // Mã voucher
}
