package com.poly.dto;

import com.poly.entity.Product;
import lombok.Data;

@Data
public class ReviewPostDto {
    private Long id;
    private int rating;
    private String title;
    private String comment;
    private Long productId;
}
