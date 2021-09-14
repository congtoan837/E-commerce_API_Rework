package com.poly.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
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
