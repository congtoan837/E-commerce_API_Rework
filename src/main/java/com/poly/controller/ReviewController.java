package com.poly.controller;

import com.poly.dto.review.ReviewGetDto;
import com.poly.dto.review.ReviewPostDto;
import com.poly.entity.Review;
import com.poly.entity.User;
import com.poly.ex.ModelMapperConfig;
import com.poly.ex.StringContent;
import com.poly.services.AuthService;
import com.poly.exception.ResponseUtils;
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

    public User getAuthUser(Authentication authentication) {
        // auth user
        AuthService auth = (AuthService) authentication.getPrincipal();
        User user = userService.getById(auth.getId());
        if (user != null) return user;
        return null;
    }

    @GetMapping("/getAllReview")
    public ResponseEntity<?> getAllReview(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "1") int size) {
        try {
            Page<Review> reviews = reviewService.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
            Page<ReviewGetDto> result = mapper.mapEntityPageIntoDtoPage(reviews, ReviewGetDto.class);
            return new ResponseEntity<>(result.getContent(), "Get reviews success!", result.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, "Get reviews fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createReview")
    public ResponseEntity<?> createUser(@RequestBody ReviewPostDto request, Authentication authentication) {
        try {
            if (StringUtils.isAnyEmpty(request.getTitle(), request.getComment())) {
                return new ResponseEntity<>(StringContent.param_not_found, HttpStatus.BAD_REQUEST);
            }
            if (request.getRating() < 1 || request.getRating() > 5) {
                return new ResponseEntity<>(null, "Rate from 1 to 5 star!", HttpStatus.BAD_REQUEST);
            }
            // auth user
            User user = getAuthUser(authentication);
            if (user == null) {
                return new ResponseEntity<>(StringContent.NOT_FOUND_USER, HttpStatus.BAD_REQUEST);
            }
            // update user
            Review review = mapper.map(request, Review.class);
            review.setUser(user);
            // save
            reviewService.save(review);
            return new ResponseEntity<>(StringContent.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateReview")
    public ResponseEntity<?> updateReview(@RequestBody ReviewPostDto request, Authentication authentication) {
        try {
            if (StringUtils.isAnyEmpty(request.getTitle(), request.getComment()) && request.getRating() < 1 || request.getRating() > 5) {
                return new ResponseEntity<>(StringContent.param_not_found, HttpStatus.BAD_REQUEST);
            }
            Review getReview = reviewService.getById(request.getId());
            if (getReview == null) {
                return new ResponseEntity<>(StringContent.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            // auth user
            User user = getAuthUser(authentication);
            if (user == null) {
                return new ResponseEntity<>(StringContent.NOT_FOUND_USER, HttpStatus.BAD_REQUEST);
            }
            if (!StringUtils.equals(user.getId().toString(), getReview.getUser().getId().toString())) throw new Exception();

            reviewService.save(mapper.map(request, Review.class));
            return new ResponseEntity<>(StringContent.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(StringContent.FAIL, HttpStatus.BAD_REQUEST);
        }
    }
}
