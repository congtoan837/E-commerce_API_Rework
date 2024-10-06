package com.poly.mapper;

import com.poly.dto.request.ProductRequest;
import com.poly.dto.response.ProductResponse;
import com.poly.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categories", ignore = true)
    Product toProduct(ProductRequest request);

    ProductResponse toProductResponse(Product Product);

    List<ProductResponse> toUserResponseList(List<Product> categories);

    @Mapping(target = "categories", ignore = true)
    void updateProductFromProductRequest(@MappingTarget Product Product, ProductRequest request);
}
