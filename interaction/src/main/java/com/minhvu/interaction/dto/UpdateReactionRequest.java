package com.minhvu.interaction.dto;

import com.minhvu.interaction.entity.enums.ReactionType;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateReactionRequest {
    private ReactionType reactionType;
    private UUID postId;
}
