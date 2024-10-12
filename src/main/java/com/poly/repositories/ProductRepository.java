package com.poly.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByIsDeletedFalse(Pageable pageable);

    Optional<Product> findByIdAndIsDeletedFalse(UUID Id);
}
