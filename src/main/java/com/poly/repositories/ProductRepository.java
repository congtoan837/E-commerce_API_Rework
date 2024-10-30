package com.poly.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false "
            + "AND ("
            + "p.name ILIKE %:keyword% OR remove_accent(p.name) ILIKE %:keyword% OR "
            + "p.description ILIKE %:keyword% OR remove_accent(p.description) ILIKE %:keyword%)")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Product> findByIsDeletedFalse(Pageable pageable);

    Optional<Product> findByIdAndIsDeletedFalse(UUID Id);
}
