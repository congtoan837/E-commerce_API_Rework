package com.poly.dto.Response;

import java.util.Set;

import lombok.Data;

@Data
public class ProductResponse {

    private Long id;

    private String name;

    private String url;

    private String shortDescription;

    private String price;

    private Set<CategoryResponse> categories;

    private String cover_image;

    private Set<String> images;

    private Set<ReviewResponse> reviews;
}
