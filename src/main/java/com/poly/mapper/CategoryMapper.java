package com.poly.mapper;

import com.poly.dto.request.CategoryRequest;
import com.poly.dto.response.CategoryResponse;
import com.poly.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toUserResponseList(List<Category> categories);

    void updateCategoryFromCategoryRequest(@MappingTarget Category category, CategoryRequest request);
}
