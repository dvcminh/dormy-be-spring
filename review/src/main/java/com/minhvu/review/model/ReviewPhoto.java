package com.minhvu.review.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review_photos")
public class ReviewPhoto extends BaseEntity{
    private String photoUrl;
    private String photoName;
    private String photoDescription;
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
