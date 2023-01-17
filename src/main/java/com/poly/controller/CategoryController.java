package com.poly.controller;

import com.poly.dto.CategoryDto;
import com.poly.dto.user.UserGetDto;
import com.poly.entity.Category;
import com.poly.ex.ModelMapperConfig;
import com.poly.services.CategoryService;
import com.poly.services.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    private ModelMapperConfig mapper;

    @GetMapping("/getPageCategory")
    public ResponseEntity<?> getPageCategory(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "1") int size, @RequestParam String sortBy,
                                            @RequestParam String sortType, @RequestParam(defaultValue = "") String search) {
        try {
            String S = sortType.trim().toLowerCase();
            Page<Category> categories = categoryService.pageSearchCategory(search, PageRequest.of(page, size, Sort.by(S.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
            Page<Object> result = categories.map(category -> mapper.map(category, UserGetDto.class));
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get category success!", categories.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getListCategory")
    public ResponseEntity<?> getListCategory(@RequestParam(defaultValue = "") String search) {
        try {
            List<Category> categoryList = categoryService.SearchCategory(search);
            return responseUtils.getResponseEntity(categoryList, "1", "Get category success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get category fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createCategory")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto request) {
        try {
            if (categoryService.findByName(request.getName()) != null) {
                return responseUtils.getResponseEntity("-1", "Category name is already exists!", HttpStatus.BAD_REQUEST);
            } else if (request.getName().length() < 3) {
                return responseUtils.getResponseEntity("-1", "Category must be at least 3 characters!", HttpStatus.BAD_REQUEST);
            } else {
                Category category = mapper.map(request, Category.class);
                categoryService.save(category);
                return responseUtils.getResponseEntity("-1", "Create category success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Create category fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto request) {
        try {
            if (request.getName().length() < 3) {
                return responseUtils.getResponseEntity("-1", "Category must be at least 3 characters!", HttpStatus.BAD_REQUEST);
            } else {
                Long id = request.getId();
                Category category = categoryService.getById(id);

                if (category != null) {
                    category.setName(request.getName());
                    categoryService.save(category);
                    return responseUtils.getResponseEntity("-1", "Update category success!", HttpStatus.OK);
                } else {
                    return responseUtils.getResponseEntity("-1", "Category not found!", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Update category fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            Category category = categoryService.getById(id);

            if (category != null) {
                categoryService.deleteById(id);
                return responseUtils.getResponseEntity("-1", "Delete category success!", HttpStatus.OK);
            } else {
                return responseUtils.getResponseEntity("-1", "Category not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }
}
