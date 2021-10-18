package com.poly.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.entity.Product;
import com.poly.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderGetDto {

    private Long id;

    private String orderPhone;

    private String orderAddress;

    private byte quantity;

    private byte Status;

    private String note;

    private Set<ProductGetDto> products;

    private UserDto user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime modifiedLastTime;
}
