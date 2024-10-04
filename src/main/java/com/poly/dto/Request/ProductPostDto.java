package com.poly.dto.Request;

import com.poly.dto.Response.CategoryResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
