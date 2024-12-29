package com.poly.ex.content;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("pending"),
    CANCELLED("cancelled");

    private final String name;
}
