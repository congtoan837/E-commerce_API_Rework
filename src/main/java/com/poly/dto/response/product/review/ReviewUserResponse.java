package com.poly.dto.response.product.review;

import lombok.Data;

import java.util.UUID;

@Data
public class ReviewUserResponse {
    private UUID id;
    private String username;
    private String name;
    private String image;
}
