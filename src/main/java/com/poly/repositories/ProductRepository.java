package com.poly.repositories;

import com.poly.entity.Product;
import com.poly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product AS p WHERE p.name LIKE %:name%")
    Page<Product> getAllProduct(String name, Pageable pageable);

    Product findByName(String name);

    Product findByUrl(String url);

    Product getById(Long id);
}
