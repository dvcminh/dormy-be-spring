package com.minhvu.friend.dto;

import com.minhvu.friend.model.enums.ReactionType;
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
    private ReactionType reactionType;
    private Date createdAt;
    private Date updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
