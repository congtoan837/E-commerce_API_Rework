package com.poly.repositories;

import com.poly.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review AS r WHERE r.product.id = :id")
    Page<Review> getReviewByProduct(Long id, Pageable pageable);

    Review getById(long id);
}
