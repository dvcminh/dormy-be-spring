package com.minhvu.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    UUID id;
    String mediaUuid;
    String filename;
    UUID userId;
    String uri;
    UUID postId;
    String fileType;
    UUID size;
    LocalDateTime createdDate;
}