package com.poly.dto.Response;

import lombok.Data;

@Data
public class ReviewResponse {

    private Long id;

    private int rating;

    private String title;

    private String comment;

    private UserReviewDto user;

    @Data
    public class UserReviewDto {

        private Long id;

        private String name;

        private String image;

        private String username;
    }
}
