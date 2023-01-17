package com.poly.dto.review;

import com.poly.dto.UserReviewDto;
import lombok.Data;

@Data
public class ReviewPostDto {

    private Long id;

    private int rating;

    private String title;

    private String comment;
}
