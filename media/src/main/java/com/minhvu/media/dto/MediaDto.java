package com.minhvu.media.dto;

import com.minhvu.media.model.Media;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link Media}
 */
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
    Long size;
    Date createdAt;
    Date updatedAt;
    UUID createdBy;
    UUID updatedBy;
}