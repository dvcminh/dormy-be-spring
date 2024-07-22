package com.minhvu.review.controller;

import com.minhvu.review.dto.model.ReviewDto;
import com.minhvu.review.dto.request.CreateReviewRequest;
import com.minhvu.review.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/review")
public class ReviewController extends BaseController{
    private final ReviewService reviewService;
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable UUID reviewId) {
        return ResponseEntity.ok(reviewService.findByReviewId(reviewId));
    }

    @GetMapping("/user/{userId}/review/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewByUserIdAndReviewId(@PathVariable UUID userId, @PathVariable UUID reviewId) {
        return ResponseEntity.ok(reviewService.findByUserIdAndReviewId(userId, reviewId));
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewDto> createReview(@RequestBody CreateReviewRequest createReviewRequest) {
        return ResponseEntity.ok(reviewService.createReview(createReviewRequest));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable UUID reviewId, @RequestBody CreateReviewRequest createReviewRequest) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, createReviewRequest));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reviewId}/rating/{rating}")
    public ResponseEntity<Void> updateRating(@PathVariable UUID reviewId, @PathVariable int rating) {
        reviewService.updateRating(reviewId, rating);
        return ResponseEntity.ok().build();
    }

}
