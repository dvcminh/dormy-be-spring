package com.minhvu.friend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {

    private UUID id;
    private UUID postId;
    private UUID userId;
    private String commentText;
    private Date createdAt;
}
