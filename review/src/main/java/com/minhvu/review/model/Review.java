package com.minhvu.review.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity{
    private String title;
    private Date date;
    private String content;
    private int rating;
    private UUID reviewer;
    @OneToMany(mappedBy = "review")
    private List<ReviewPhoto> reviewPhotoList;
}
