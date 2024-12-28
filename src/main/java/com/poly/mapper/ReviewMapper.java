package com.poly.mapper;

import com.poly.dto.request.ReviewRequest;
import com.poly.dto.response.product.review.ReviewResponse;
import com.poly.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(ReviewRequest request);

    ReviewResponse toReviewResponse(Review review);

    void updateReviewFromReviewRequest(@MappingTarget Review review, ReviewRequest request);
}
