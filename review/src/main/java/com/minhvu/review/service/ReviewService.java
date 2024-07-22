package com.minhvu.review.service;

import com.minhvu.review.dto.model.ReviewDto;
import com.minhvu.review.dto.request.CreateReviewRequest;

import java.util.UUID;

public interface ReviewService {
    ReviewDto findByReviewId(UUID reviewId);
    ReviewDto findByUserIdAndReviewId(UUID userId, UUID reviewId);
    ReviewDto createReview(CreateReviewRequest createReviewRequest);
    ReviewDto updateReview(UUID reviewId, CreateReviewRequest createReviewRequest);
    void deleteReview(UUID reviewId);
    // update rating
    void updateRating(UUID reviewId, int rating);
}
