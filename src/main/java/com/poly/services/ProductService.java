package com.poly.services;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poly.dto.request.ProductRequest;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.ProductResponse;
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

    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toProduct(request);

        List<Category> categories = categoryRepository.findAllById(request.getCategories());
        product.setCategories(new HashSet<>(categories));

        return productMapper.toProductResponse(product);
    }

    public PageResponse<ProductResponse> getAll(String keyword, int page, int size) {
        if (StringUtils.isNotBlank(keyword))
            keyword = StringUtils.lowerCase(keyword).trim();

        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageResult = productRepository.searchByKeyword(keyword, pageable);
        return productMapper.toPageResponse(pageResult);
    }

    public ProductResponse update(ProductRequest request) {
        Product product =
                productRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        productMapper.updateProductFromProductRequest(product, request);

        List<Category> categories = categoryRepository.findAllById(request.getCategories());
        product.setCategories(new HashSet<>(categories));

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void delete(UUID id) {
        Product product = productRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        product.setDeleted(true);

        productRepository.save(product);
    }
}
