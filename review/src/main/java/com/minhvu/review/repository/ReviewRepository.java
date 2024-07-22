package com.minhvu.review.repository;

import com.minhvu.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Review findByReviewer(UUID reviewId);
    Review findByIdAndReviewer(UUID userId, UUID reviewId);
    Review save(Review review);
    void deleteByReviewer(UUID reviewId);
//    void updateRating(UUID reviewId, int rating);
}
