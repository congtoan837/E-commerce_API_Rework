package com.poly.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ProductDetailGetDto {

    private Long id;

    private String name;

    private String url;

    private String shortDescription;

    private String price;

    private Set<CategoryDto> categories;

    private Set<ImageGetDto> images;

    private Set<ReviewGetDto> reviews;
}
