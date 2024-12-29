package com.poly.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.CategoryRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.product.CategoryResponse;
import com.poly.services.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.create(request))
                .build();
    }

    @GetMapping("/get")
    public ApiResponse<List<CategoryResponse>> getAllCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAll())
                .build();
    }

    @DeleteMapping("/delete/{categoryName}")
    public ApiResponse<Boolean> deleteCategory(@PathVariable @Valid String categoryName) {
        categoryService.delete(categoryName);
        return ApiResponse.<Boolean>builder().result(Boolean.TRUE).build();
    }
}
