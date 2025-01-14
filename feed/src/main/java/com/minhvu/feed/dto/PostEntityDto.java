package com.minhvu.feed.dto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostEntityDto implements Serializable {
    @Id
    UUID id;
    String body;
    Boolean isDeleted;
    UUID userId;
    Collection<String> urlsMedia;
    Date createdAt;
    Date updatedAt;
    UUID createdBy;
    UUID updatedBy;
}