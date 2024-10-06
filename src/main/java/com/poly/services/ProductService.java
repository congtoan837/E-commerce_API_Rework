package com.poly.services;

import com.poly.dto.request.ProductRequest;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    
    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productMapper.toUserResponseList(
                productRepository.findByIsDeletedFalse(pageable).getContent());
    }

    public ProductResponse update(ProductRequest request) {
        Product product =
                productRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        productMapper.updateProductFromProductRequest(product, request);

        List<Category> categories = categoryRepository.findAllById(request.getCategories());
        product.setCategories(Set.copyOf(categories));

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void delete(UUID id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        product.setDeleted(true);

        productRepository.save(product);
    }
}
