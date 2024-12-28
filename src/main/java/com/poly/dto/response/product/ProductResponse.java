package com.poly.dto.response.product;

import java.util.Set;
import java.util.UUID;

import com.poly.dto.response.product.review.ReviewResponse;
import lombok.Data;

@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private Set<CategoryResponse> categories;
    private Set<VariantResponse> variants;
    private String coverImage;
    private Set<String> images;
    private Set<ReviewResponse> reviews;
}
