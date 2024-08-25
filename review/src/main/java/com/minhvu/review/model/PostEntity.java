package com.minhvu.review.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity extends BaseEntity{

    private String body;

    private UUID userId;
    private boolean isDeleted;
}
