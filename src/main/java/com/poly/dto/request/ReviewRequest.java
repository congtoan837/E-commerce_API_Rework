package com.poly.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {

    private Long id;

    private int rating;

    private String title;

    private String comment;
}
