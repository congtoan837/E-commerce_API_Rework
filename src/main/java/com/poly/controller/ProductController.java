package com.poly.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.ProductRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.product.ProductResponse;
import com.poly.services.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

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
}
