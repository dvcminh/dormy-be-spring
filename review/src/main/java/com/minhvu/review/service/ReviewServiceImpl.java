package com.minhvu.review.service;

import com.minhvu.review.dto.mapper.ReviewMapper;
import com.minhvu.review.dto.model.ReviewDto;
import com.minhvu.review.dto.request.CreateReviewRequest;
import com.minhvu.review.model.Review;
import com.minhvu.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    @Override
    public ReviewDto findByReviewId(UUID reviewId) {
        return reviewMapper.toDto(reviewRepository.findByReviewer(reviewId));
    }

    @Override
    public ReviewDto findByUserIdAndReviewId(UUID userId, UUID reviewId) {
        return reviewMapper.toDto(reviewRepository.findByIdAndReviewer(userId, reviewId));
    }

    @Override
    public ReviewDto createReview(CreateReviewRequest createReviewRequest) {
        Review createRevew = Review.builder()
                .reviewer(createReviewRequest.getReviewer())
                .content(createReviewRequest.getContent())
                .title(createReviewRequest.getTitle())
                .reviewPhotoList(createReviewRequest.getReviewPhotoList())
                .build();
        return reviewMapper.toDto(reviewRepository.save(createRevew));
    }

    @Override
    public ReviewDto updateReview(UUID reviewId, CreateReviewRequest createReviewRequest) {
        Review review = reviewRepository.findByReviewer(reviewId);
        review.setContent(createReviewRequest.getContent());
        review.setTitle(createReviewRequest.getTitle());
        review.setReviewPhotoList(createReviewRequest.getReviewPhotoList());
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(UUID reviewId) {
        reviewRepository.deleteByReviewer(reviewId);
    }

    @Override
    public void updateRating(UUID reviewId, int rating) {
//        reviewRepository.updateRating(reviewId, rating);
    }
}
