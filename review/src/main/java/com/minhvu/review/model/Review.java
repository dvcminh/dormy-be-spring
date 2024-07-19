package com.minhvu.review.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity{
    private String title;
    private Date date;

}
