package com.poly.dto;

import com.poly.entity.Product;
import com.poly.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class OrderPostDto {
    private Long id;

    private String orderPhone;

    private String orderAddress;

    private byte quantity;

    private String note;

    private Set<ProductPostDto> products;

    private UserPostDto user;
}
