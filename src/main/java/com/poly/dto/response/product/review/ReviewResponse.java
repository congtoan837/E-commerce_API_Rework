package com.poly.dto.response.product.review;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {
    private UUID id;
    private int rating;
    private String title;
    private String comment;
    private ReviewUserResponse user;
}
