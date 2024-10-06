package com.poly.services;

import com.poly.dto.request.CategoryRequest;
import com.poly.dto.response.CategoryResponse;
import com.poly.entity.Category;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.CategoryMapper;
import com.poly.repositories.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse create(CategoryRequest request) {
        Category category = categoryMapper.toCategory(request);
        return categoryMapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryMapper.toUserResponseList(
                categoryRepository.findAll(pageable).getContent());
    }

    public CategoryResponse update(CategoryRequest request) {
        Category category =
                categoryRepository.findById(request.getName()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        categoryMapper.updateCategoryFromCategoryRequest(category, request);

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public void delete(String id) {
        categoryRepository.deleteById(id);
    }
}
