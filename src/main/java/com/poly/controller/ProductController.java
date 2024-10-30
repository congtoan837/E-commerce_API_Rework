package com.poly.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.CategoryRequest;
import com.poly.dto.request.ProductRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.CategoryResponse;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.ProductResponse;
import com.poly.services.CategoryService;
import com.poly.services.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    ProductService productService;
    CategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.create(request))
                .build();
    }

    @GetMapping("/get")
    public ApiResponse<PageResponse<ProductResponse>> getAllProduct(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getAll(keyword, page, size))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<ProductResponse> updateProduct(@RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.update(request))
                .build();
    }

    @DeleteMapping("/delete/{productName}")
    public ApiResponse<Boolean> deleteProduct(@PathVariable @Valid UUID productName) {
        productService.delete(productName);
        return ApiResponse.<Boolean>builder().result(Boolean.TRUE).build();
    }

    @PostMapping("/category/create")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.create(request))
                .build();
    }

    @GetMapping("/category/get")
    public ApiResponse<List<CategoryResponse>> getAllCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAll())
                .build();
    }

    @DeleteMapping("/category/delete/{categoryName}")
    public ApiResponse<Boolean> deleteCategory(@PathVariable @Valid String categoryName) {
        categoryService.delete(categoryName);
        return ApiResponse.<Boolean>builder().result(Boolean.TRUE).build();
    }
}
