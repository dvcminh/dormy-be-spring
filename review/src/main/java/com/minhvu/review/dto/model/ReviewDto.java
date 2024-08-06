package com.minhvu.review.dto.model;

import com.minhvu.review.model.ReviewPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ReviewDto {
    private UUID id;
    private UUID reviewer;
    private String title;
    private Date date;
    private String content;
    private int rating;
    @OneToMany(mappedBy = "review")
    private List<ReviewPhoto> reviewPhotoList;
    private Date createdAt;
}
