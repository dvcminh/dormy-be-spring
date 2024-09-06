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
    UUID postId;
    UUID userId;
    String mediaUuid;
    String filename;
    String uri;
    String fileType;
    long size;
    LocalDateTime createdDate;
}