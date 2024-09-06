package com.minhvu.feed.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class ReactionDto {
    private UUID id;
    private UUID postId;
    private UUID userId;
    private ReactionTypeDto reactionType;
    private Date createdAt;
    private Date updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
