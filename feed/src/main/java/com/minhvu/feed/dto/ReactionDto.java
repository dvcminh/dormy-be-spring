package com.minhvu.feed.dto;

import java.time.LocalDateTime;

public class ReactionDto {
    private Long id;
    private Long postId;
    private Long userId;
    private ReactionTypeDto reactionType;
    private LocalDateTime createdAt;
}
