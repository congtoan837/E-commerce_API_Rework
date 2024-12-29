package com.poly.services;

import java.util.List;
import java.util.Objects;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.poly.dto.request.ReviewRequest;
import com.poly.dto.response.product.review.ReviewResponse;
import com.poly.entity.Product;
import com.poly.entity.Review;
import com.poly.entity.User;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.ReviewMapper;
import com.poly.repositories.ProductRepository;
import com.poly.repositories.ReviewRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    UserService userService;

    ReviewRepository reviewRepository;
    ProductRepository productRepository;

    ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse create(ReviewRequest request) {
        Product product = productRepository
                .findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        User user = userService.getCurrentUser();

        Review review = reviewMapper.toReview(request);
        review.setProduct(product);
        review.setUser(user);

        // Lưu review vào database
        reviewRepository.save(review);

        // Tính toán lại `rating` và cập nhật sản phẩm
        product.setRating(this.calculatorRating(product));
        productRepository.save(product);

        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Transactional
    public void delete(ReviewRequest request) {
        Product product = productRepository
                .findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        User user = userService.getCurrentUser();

        Review review = reviewRepository
                .findByIdAndIsDeletedFalse(request.getId())
                .filter(r -> Objects.equals(r.getUser().getId(), user.getId()))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        reviewRepository.delete(review);

        // Tính toán lại `rating` và cập nhật sản phẩm
        product.setRating(this.calculatorRating(product));
        productRepository.save(product);
    }

    private double calculatorRating(Product product) {
        // Lấy danh sách các review của sản phẩm
        List<Review> reviews = reviewRepository.findAllByProductIdAndIsDeletedFalse(product.getId());

        if (reviews.isEmpty()) {
            return 5.0; // Nếu chưa có review, trả về 5.0
        }

        // Tính tổng số điểm và số lượng review
        double totalRating = 0.0;
        for (Review review : reviews) {
            totalRating += review.getRating(); // `getRating()` trả về số sao của review
        }

        // Tính trung bình
        return totalRating / reviews.size();
    }
}
