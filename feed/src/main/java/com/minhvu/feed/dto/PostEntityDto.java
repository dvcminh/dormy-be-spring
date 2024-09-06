package com.minhvu.feed.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostEntityDto implements Serializable {
    @Id
    UUID id;
    String body;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isDeleted;
    UUID userId;
}