package com.minhvu.feed.dto;

import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
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
    UUID createdBy;
    UUID updatedBy;
}