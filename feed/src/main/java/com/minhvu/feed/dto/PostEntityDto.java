package com.minhvu.feed.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostEntityDto implements Serializable {

    Long id;
    String body;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isDeleted;
    Long userId;
}