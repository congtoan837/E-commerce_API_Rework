package com.poly.controller;

import com.poly.dto.*;
import com.poly.entity.*;
import com.poly.services.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/getAllReview")
    public ResponseEntity<?> getAllReview(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy,
                                          @RequestParam String sortType, @RequestParam Long productId) {
        try {
            String S = sortType.trim().toLowerCase();
            Page<Review> reviews = reviewService.getReviewByProduct(productId, PageRequest.of(page, size, Sort.by(S.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
            Page<Object> result = reviews.map(review -> mapper.map(review, ReviewGetDto.class));
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get review success!", reviews.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get review fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createReview")
    public ResponseEntity<?> createUser(@RequestBody ReviewPostDto request, Authentication authentication) {
        try {
            AuthService auth = (AuthService) authentication.getPrincipal();
            User user = userService.getById(auth.getId());

            if (request.getTitle().length() == 0 || request.getComment().length() == 0) {
                return responseUtils.getResponseEntity("-1", "Create user fail!", HttpStatus.BAD_REQUEST);
            } else if (request.getRating() <= 0 || request.getRating() > 5) {
                return responseUtils.getResponseEntity("-1", "Please! rate from 1 to 5 star", HttpStatus.BAD_REQUEST);
            } else {
                Review review = mapper.map(request, Review.class);

                Product product = new Product();
                product.setId(request.getProductId());

                review.setProduct(product);
                review.setUser(user);
                reviewService.save(review);
                return responseUtils.getResponseEntity("1", "Create review success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Create review fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateReview")
    public ResponseEntity<?> updateReview(@RequestBody Review review, Authentication authentication) {
        try {
            if (review.getTitle().length() == 0 || review.getComment().length() == 0) {
                return responseUtils.getResponseEntity("-1", "Create user fail!", HttpStatus.BAD_REQUEST);
            } else if (review.getRating() <= 0 || review.getRating() > 5) {
                return responseUtils.getResponseEntity("-1", "Please! rate from 1 to 5 star", HttpStatus.BAD_REQUEST);
            } else {
                Long id = review.getId();
                Review getReview = reviewService.getById(id);

                AuthService auth = (AuthService) authentication.getPrincipal();

                if (getReview != null) {
                    if (getReview.getUser().getId().equals(auth.getId())) {

                        getReview.setRating(review.getRating());
                        getReview.setTitle(review.getTitle());
                        getReview.setComment(review.getComment());

                        reviewService.save(getReview);
                        return responseUtils.getResponseEntity("1", "Update review success!", HttpStatus.OK);
                    } else {
                        return responseUtils.getResponseEntity("-1", "You not have permission to access!", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return responseUtils.getResponseEntity("-1", "Review " + id + " not found!", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Update review fail!", HttpStatus.BAD_REQUEST);
        }
    }
}
