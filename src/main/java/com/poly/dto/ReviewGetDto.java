package com.poly.dto;

import lombok.Data;

@Data
public class ReviewGetDto {

    private Integer id;

    private int rating;

    private String title;

    private String comment;

    private UserReviewDto user;
}
