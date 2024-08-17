package com.minhvu.feed.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SharedDto {
    private Long id;
    private Long postId;
    private Long userId;
    private LocalDateTime sharedAt;
}
