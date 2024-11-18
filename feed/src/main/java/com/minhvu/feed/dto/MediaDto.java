package com.minhvu.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MediaDto implements Serializable {
    UUID id;
    UUID userId;
    UUID postId;
    String mediaUuid;
    String filename;
    String uri;
    String fileType;
    Date createdAt;
    Date updatedAt;
    UUID createdBy;
    UUID updatedBy;
}