package com.poly.dto.product;

import com.poly.dto.CategoryDto;
import com.poly.dto.image.ImageGetDto;
import lombok.Data;

import java.util.Set;

@Data
public class ProductPostDto {

    private Long id;

    private String name;

    private String note;

    private Long price;

    private Set<CategoryDto> categories;

    private Set<ImageGetDto> images;
}
