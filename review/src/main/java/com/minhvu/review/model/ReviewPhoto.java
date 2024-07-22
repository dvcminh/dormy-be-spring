package com.minhvu.review.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class ReviewPhoto extends BaseEntity{
    private String photoUrl;
    private String photoName;
    private String photoDescription;
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
