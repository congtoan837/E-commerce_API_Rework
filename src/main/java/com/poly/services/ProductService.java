package com.poly.services;

import java.util.*;

import com.poly.dto.request.VariantRequest;
import com.poly.entity.Variant;
import com.poly.mapper.VariantMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poly.dto.request.ProductRequest;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.product.ProductResponse;
import com.poly.entity.Category;
import com.poly.entity.Product;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.ProductMapper;
import com.poly.repositories.CategoryRepository;
import com.poly.repositories.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    ProductMapper productMapper;
    VariantMapper variantMapper;

    private void processVariant(ProductRequest request, Product product) {
        if (request.getVariants().isEmpty()) {
            product.setMinPrice(null);
            product.setMaxPrice(null);
            // Đánh dấu các variant cũ là "đã xóa"
            product.getVariants().clear();
            return;
        }

        long minPrice = Long.MAX_VALUE;
        long maxPrice = Long.MIN_VALUE;

        // Lấy collection hiện tại
        Set<Variant> variants = product.getVariants();
        variants.clear(); // Xóa tất cả phần tử cũ
        for (VariantRequest variantRequest : request.getVariants()) {
            long price = variantRequest.getPrice();

            if (price < minPrice) {
                minPrice = price;
            }
            if (price > maxPrice) {
                maxPrice = price;
            }

            Variant variant = variantMapper.toVariant(variantRequest);
            variant.setProduct(product);
            variants.add(variant);
        }

        product.setVariants(variants);
        product.setMinPrice(minPrice);
        product.setMaxPrice(maxPrice);
    }

    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toProduct(request);

        List<Category> categories = categoryRepository.findAllById(request.getCategories());
        product.setCategories(new HashSet<>(categories));

        this.processVariant(request, product);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public PageResponse<ProductResponse> getAll(String keyword, int page, int size) {
        if (StringUtils.isNotBlank(keyword))
            keyword = StringUtils.lowerCase(keyword).trim();

        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageResult = productRepository.searchByKeyword(keyword, pageable);
        return productMapper.toPageResponse(pageResult);
    }

    @Transactional
    public ProductResponse update(ProductRequest request) {
        Product currentProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Vô hiệu hóa phiên bản cũ
        currentProduct.setDeleted(true);
        productRepository.save(currentProduct);

        // Tạo sản phẩm mới
        var product = productMapper.copyProduct(currentProduct);
        productMapper.updateProductFromProductRequest(product, request);

        // Cập nhật các danh mục cho sản phẩm
        List<Category> categories = categoryRepository.findAllById(request.getCategories());
        product.setCategories(new HashSet<>(categories));

        // Xử lý biến thể sản phẩm (nếu có)
        this.processVariant(request, product);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void delete(UUID id) {
        Product product = productRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setDeleted(true);
        product.getVariants().forEach(variant -> variant.setDeleted(true));

        productRepository.save(product);
    }
}
