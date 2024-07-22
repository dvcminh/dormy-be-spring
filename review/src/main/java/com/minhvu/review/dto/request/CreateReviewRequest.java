package com.minhvu.review.dto.request;

import com.minhvu.review.model.ReviewPhoto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateReviewRequest {
    private UUID reviewer;
    private String title;
    private String content;
    private List<ReviewPhoto> reviewPhotoList;
}
