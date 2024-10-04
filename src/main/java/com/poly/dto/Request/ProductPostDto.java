package com.poly.dto.Request;

import java.util.Set;

import com.poly.dto.Response.CategoryResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPostDto {

    private Long id;

    private String name;

    private String note;

    private Long price;

    private Set<CategoryResponse> categories;

    private String cover_image;

    private Set<String> images;
}
