package com.poly.services;

import com.poly.entity.Review;
import com.poly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {
    Review getById(long id);

    Page<Review> findAll(Pageable pageable);

    Review save(Review entity);

    Optional<Review> findById(Long aLong);

    long count();

    void deleteById(Long aLong);
}
