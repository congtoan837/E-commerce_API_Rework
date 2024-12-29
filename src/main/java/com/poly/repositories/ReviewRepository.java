package com.poly.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findByIdAndIsDeletedFalse(UUID id);

    List<Review> findAllByProductIdAndIsDeletedFalse(UUID id);
}
