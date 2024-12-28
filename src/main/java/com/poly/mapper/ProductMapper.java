package com.poly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.poly.dto.request.ProductRequest;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.product.ProductResponse;
import com.poly.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categories", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "categories", ignore = true)
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

    // Phương thức sao chép từ sản phẩm cũ sang sản phẩm mới
    @Mapping(target = "id", ignore = true) // Bỏ qua ID để MapStruct không ánh xạ
    @Mapping(target = "variants", ignore = true) // Biến thể được xử lý riêng
    @Mapping(target = "createTime", ignore = true) // Không sao chép thời gian tạo
    @Mapping(target = "modifiedLastTime", ignore = true) // Không sao chép thời gian cập nhật
    @Mapping(target = "isDeleted", constant = "false") // Thiết lập giá trị mặc định
    Product copyProduct(Product oldProduct);
}
