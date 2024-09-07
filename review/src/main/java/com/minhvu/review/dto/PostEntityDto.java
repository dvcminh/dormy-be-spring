package com.minhvu.review.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostEntityDto implements Serializable {

    UUID id;
    String body;
    Date createdAt;
    Date updatedAt;
    boolean isDeleted;
    UUID userId;
}