package com.minhvu.review.dto.mapper;

import com.minhvu.review.dto.model.ReviewDto;
import com.minhvu.review.model.Review;
import org.mapstruct.Mapper;

@Mapper
public interface ReviewMapper {
    // ReviewDto to Review
    Review toModel(ReviewDto reviewDto);
    // Review to ReviewDto
    ReviewDto toDto(Review review);
}
