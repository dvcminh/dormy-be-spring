package com.minhvu.review.dto;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostEntityDto implements Serializable {

    UUID id;
    String body;
    boolean isDeleted;
    UUID userId;
    Date createdAt;

    Date updatedAt;

    UUID createdBy;

    UUID updatedBy;
}