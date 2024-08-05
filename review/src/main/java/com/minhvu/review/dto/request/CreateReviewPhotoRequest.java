package com.minhvu.review.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateReviewPhotoRequest {
    private String photoUrl;
    private String photoDescription;
    private UUID reviewId;
    private String photoName;
}
