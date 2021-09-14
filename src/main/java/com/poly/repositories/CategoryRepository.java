package com.poly.repositories;

import com.poly.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category AS c WHERE c.name LIKE %:name%")
    Page<Category> pageSearchCategory(String name, Pageable pageable);

    @Query("SELECT c FROM Category AS c WHERE c.name LIKE %:name%")
    List<Category> searchCategory(String name);

    Category findByName(String name);

    Category getById(Long id);
}
