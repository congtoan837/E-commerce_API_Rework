package com.poly.services.imp;

import com.poly.entity.Category;
import com.poly.repositories.CategoryRepository;
import com.poly.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    @Query("SELECT c FROM Category AS c WHERE c.name LIKE %:name%")
    public Page<Category> pageSearchCategory(String name, Pageable pageable) {
        return categoryRepository.pageSearchCategory(name, pageable);
    }

    @Override
    public List<Category> SearchCategory(String name) {
        return categoryRepository.searchCategory(name);
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.getById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public void deleteById(Long aLong) {
        categoryRepository.deleteById(aLong);
    }

}
