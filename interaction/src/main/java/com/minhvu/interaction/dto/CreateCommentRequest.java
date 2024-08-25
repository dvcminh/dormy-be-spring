package com.minhvu.interaction.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CreateCommentRequest {
    private UUID postId;
    private String commentText;
}
