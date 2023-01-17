package com.poly.dto.product;

import com.poly.dto.CategoryDto;
import com.poly.dto.image.ImageGetDto;
import com.poly.dto.review.ReviewGetDto;
import lombok.Data;

import java.util.Set;

@Data
public class ProductGetDto {

    private Long id;

    private String name;

    private String url;

    private String shortDescription;

    private String price;

    private Set<CategoryDto> categories;

    private Set<ImageGetDto> images;

    private Set<ReviewGetDto> reviews;
}
