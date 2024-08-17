package com.minhvu.feed.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentDto {
    private Long id;
    private Long postId;
    private Long userId;
    private String commentText;
    private LocalDateTime createdAt;
}
