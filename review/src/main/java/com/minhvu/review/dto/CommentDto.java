package com.minhvu.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private UUID id;
    private UUID postId;
    private UUID userId;
    private String commentText;
    private LocalDateTime createdAt;
}
