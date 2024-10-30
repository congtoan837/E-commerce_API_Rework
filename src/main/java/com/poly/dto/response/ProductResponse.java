package com.poly.dto.response;

import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class ProductResponse {

    private UUID id;

    private String name;

    private String description;

    private String price;

    private String status;

    private Set<CategoryResponse> categories;

    private String coverImage;

    private Set<String> images;

    private Set<ReviewResponse> reviews;
}
