package com.minhvu.review.dto.inter;

import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
}
