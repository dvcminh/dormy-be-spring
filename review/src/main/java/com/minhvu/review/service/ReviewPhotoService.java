package com.minhvu.review.service;

import com.minhvu.review.dto.request.CreateReviewPhotoRequest;
import com.minhvu.review.model.ReviewPhoto;

public interface ReviewPhotoService {
    public ReviewPhoto createReviewPhoto(CreateReviewPhotoRequest createReviewPhotoRequest);
}
