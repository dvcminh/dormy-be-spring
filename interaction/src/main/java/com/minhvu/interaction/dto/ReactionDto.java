package com.minhvu.interaction.dto;

import com.minhvu.interaction.entity.enums.ReactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    private Date createdAt;
    private Date updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
