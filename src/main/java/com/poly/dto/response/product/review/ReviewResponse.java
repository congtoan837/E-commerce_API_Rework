package com.poly.dto.response.product.review;

import java.util.UUID;

import lombok.Data;

@Data
public class
ReviewResponse {
    private UUID id;
    private int rating;
    private String title;
    private String comment;
    private ReviewUserResponse user;
}
