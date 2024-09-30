package com.poly.controller;

import com.poly.dto.CategoryDto;
import com.poly.dto.user.UserGetDto;
import com.poly.entity.Category;
import com.poly.ex.ModelMapperConfig;
import com.poly.ex.StringContent;
import com.poly.services.CategoryService;
import com.poly.exception.ResponseUtils;
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
    ModelMapperConfig mapper;

    @GetMapping("/getPageCategory")
    public ResponseEntity<?> getPageCategory(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "1") int size, @RequestParam String sortBy,
                                             @RequestParam(defaultValue = "1") int sortType, @RequestParam(defaultValue = "") String search) {
        try {
            Sort.Direction StringSort = (sortType == 1) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Page<Category> categories = categoryService.pageSearchCategory(search, PageRequest.of(page, size, Sort.by(StringSort, sortBy)));
            Page<Object> result = categories.map(category -> mapper.map(category, UserGetDto.class));
            return new ResponseEntity<>(result.getContent(), StringContent.SUCCESS, categories.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getListCategory")
    public ResponseEntity<?> countCategory(@RequestParam(defaultValue = "") String search) {
        try {
            List<Category> categoryList = categoryService.SearchCategory(search);
            return new ResponseEntity<>(categoryList, StringContent.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createCategory")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto request) {
        try {
            if (categoryService.findByName(request.getName()) != null) {
                return new ResponseEntity<>(null, "Category name is already exists!", HttpStatus.BAD_REQUEST);
            } else if (request.getName().length() < 3) {
                return new ResponseEntity<>(null, "Category must be at least 3 characters!", HttpStatus.BAD_REQUEST);
            } else {
                Category category = mapper.map(request, Category.class);
                categoryService.save(category);
                return new ResponseEntity<>(null, StringContent.SUCCESS, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto request) {
        try {
            if (request.getName().length() < 3) {
                return new ResponseEntity<>("Category must be at least 3 characters!", HttpStatus.BAD_REQUEST);
            } else {
                Long id = request.getId();
                Category category = categoryService.getById(id);

                if (category != null) {
                    category.setName(request.getName());
                    categoryService.save(category);
                    return new ResponseEntity<>(StringContent.SUCCESS, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(StringContent.FAIL, HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            Category category = categoryService.getById(id);
            if (category != null) {
                categoryService.deleteById(id);
                return new ResponseEntity<>(StringContent.SUCCESS, HttpStatus.OK);
            }

            return new ResponseEntity<>(StringContent.NOT_FOUND, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }
}
