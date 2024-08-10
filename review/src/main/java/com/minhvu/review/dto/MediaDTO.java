package com.minhvu.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    Long id;
    String mediaUuid;
    String filename;
    Long userId;
    String uri;
    Long postId;
    String fileType;
    Long size;
    LocalDateTime createdDate;
}