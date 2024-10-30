package com.poly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.poly.dto.request.ProductRequest;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.ProductResponse;
import com.poly.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categories", ignore = true)
    Product toProduct(ProductRequest request);

    ProductResponse toProductResponse(Product Product);

    List<ProductResponse> toProductResponseList(List<Product> categories);

    @Mapping(target = "categories", ignore = true)
    void updateProductFromProductRequest(@MappingTarget Product Product, ProductRequest request);

    // Chuyển Page<Product> thành PageResponse<ProductResponse>
    default PageResponse<ProductResponse> toPageResponse(Page<Product> productPage) {
        return PageResponse.<ProductResponse>builder()
                .totalPages(productPage.getTotalPages())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .data(productPage.map(this::toProductResponse).getContent()) // Tự động map từng phần tử
                .build();
    }
}
