package com.poly.dto.response.product.review;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUserResponse {
    private UUID id;
    private String username;
    private String name;
    private String image;
}
