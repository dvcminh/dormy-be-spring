package com.minhvu.review.service;

import com.minhvu.review.dto.request.CreateReviewPhotoRequest;
import com.minhvu.review.dto.request.CreateReviewRequest;
import com.minhvu.review.model.ReviewPhoto;
import com.minhvu.review.repository.ReviewPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewPhotoServiceImpl implements ReviewPhotoService{
    private final ReviewPhotoRepository reviewPhotoRepository;
    @Override
    public ReviewPhoto createReviewPhoto(CreateReviewPhotoRequest createReviewRequest) {
        ReviewPhoto reviewPhoto = new ReviewPhoto();
        reviewPhoto.setPhotoUrl(createReviewRequest.getPhotoUrl());
        reviewPhoto.setPhotoDescription(createReviewRequest.getPhotoDescription());
        return reviewPhotoRepository.save(reviewPhoto);
    }
}
