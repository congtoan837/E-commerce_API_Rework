package com.poly.dto.response;

import java.util.UUID;

import lombok.Data;

@Data
public class ReviewResponse {

    private UUID id;

    private int rating;

    private String title;

    private String comment;

    private UserReview userReview;

    @Data
    public class UserReview {

        private Long id;

        private String name;

        private String image;

        private String username;
    }
}
