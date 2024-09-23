package com.minhvu.feed.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostEntityDto implements Serializable {
    @Id
    UUID id;
    String body;
    Date createdAt;
    Date updatedAt;
    boolean isDeleted;
    UUID userId;
}