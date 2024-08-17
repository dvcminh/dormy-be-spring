package com.minhvu.feed.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ReactionDto {
    private Long id;
    private Long postId;
    private Long userId;
    private ReactionTypeDto reactionType;
    private LocalDateTime createdAt;
}
