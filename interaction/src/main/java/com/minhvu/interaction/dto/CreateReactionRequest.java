package com.minhvu.interaction.dto;

import com.minhvu.interaction.entity.enums.ReactionType;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CreateReactionRequest {
    private UUID postId;
    private ReactionType reactionType;
}
