package com.poly.services;

import com.poly.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryService {

    @Query("SELECT c FROM Category AS c WHERE c.name LIKE %:name%")
    Page<Category> pageSearchCategory(String name, Pageable pageable);

    @Query("SELECT c FROM Category AS c WHERE c.name LIKE %:name%")
    List<Category> SearchCategory(String name);

    Category findByName(String name);

    Category getById(Long id);

    List<Category> findAll();

    Category save(Category entity);

    void deleteById(Long aLong);
}
