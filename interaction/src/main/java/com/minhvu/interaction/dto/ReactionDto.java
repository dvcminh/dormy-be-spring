package com.minhvu.interaction.dto;

import com.minhvu.interaction.entity.enums.ReactionType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReactionDto {

    private UUID id;
    private UUID postId;
    private UUID userId;
    private ReactionType reactionType;
    private Date createdAt;
}
