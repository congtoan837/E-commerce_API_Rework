package com.poly.controller;

import com.poly.dto.review.ReviewGetDto;
import com.poly.dto.review.ReviewPostDto;
import com.poly.entity.Review;
import com.poly.entity.User;
import com.poly.ex.ModelMapperConfig;
import com.poly.services.AuthService;
import com.poly.services.ResponseUtils;
import com.poly.services.ReviewService;
import com.poly.services.UserService;
import org.apache.commons.lang3.StringUtils;
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
    private ModelMapperConfig mapper;

    @GetMapping("/getAllReview")
    public ResponseEntity<?> getAllReview(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "1") int size) {
        try {
            Page<Review> reviews = reviewService.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
            Page<ReviewGetDto> result = mapper.mapEntityPageIntoDtoPage(reviews, ReviewGetDto.class);
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get reviews success!", result.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get reviews fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createReview")
    public ResponseEntity<?> createUser(@RequestBody ReviewPostDto request, Authentication authentication) {
        try {
            AuthService auth = (AuthService) authentication.getPrincipal();
            User user = userService.getById(auth.getId());
            if (user == null) {
                return responseUtils.getResponseEntity("-1", "Not found user!", HttpStatus.BAD_REQUEST);
            }

            if (StringUtils.isAnyEmpty(request.getTitle(), request.getComment())) {
                return responseUtils.getResponseEntity("-1", "Required field cant be null!", HttpStatus.BAD_REQUEST);
            }
            if (request.getRating() < 1 || request.getRating() > 5) {
                return responseUtils.getResponseEntity("-1", "Rate from 1 to 5 star!", HttpStatus.BAD_REQUEST);
            }
            // update user
            Review review = mapper.map(request, Review.class);
            review.setUser(user);
            // save
            reviewService.save(review);
            return responseUtils.getResponseEntity("1", "Create review success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Create review fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateReview")
    public ResponseEntity<?> updateReview(@RequestBody ReviewPostDto request, Authentication authentication) {
        try {
            AuthService auth = (AuthService) authentication.getPrincipal();
            User user = userService.getById(auth.getId());
            if (user == null) {
                return responseUtils.getResponseEntity("-1", "Not found user!", HttpStatus.BAD_REQUEST);
            }

            if (StringUtils.isAnyEmpty(request.getTitle(), request.getComment())) {
                return responseUtils.getResponseEntity("-1", "Required field cant be null!", HttpStatus.BAD_REQUEST);
            }
            if (request.getRating() < 1 || request.getRating() > 5) {
                return responseUtils.getResponseEntity("-1", "Rate from 1 to 5 star!", HttpStatus.BAD_REQUEST);
            }

            Review getReview = reviewService.getById(request.getId());
            if (getReview != null) {
                return responseUtils.getResponseEntity("-1", "Review not found!", HttpStatus.BAD_REQUEST);
            }

            if (getReview.getUser().getId().equals(auth.getId())) {
                return responseUtils.getResponseEntity("-1", "You not have permission to access!", HttpStatus.BAD_REQUEST);
            }

            reviewService.save(mapper.map(request, Review.class));
            return responseUtils.getResponseEntity("1", "Update review success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }
}
