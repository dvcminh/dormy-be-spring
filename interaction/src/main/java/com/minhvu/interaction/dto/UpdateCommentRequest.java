package com.minhvu.interaction.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateCommentRequest {
    private String commentText;
    private UUID postId;
}
