package com.minhvu.interaction.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {

    private UUID id;
    private UUID postId;
    private UUID userId;
    private String commentText;
    private Date createdAt;
}
