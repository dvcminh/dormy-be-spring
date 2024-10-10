package com.minhvu.feed.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity extends BaseEntity {

    private String body;

    private UUID userId;
    @ElementCollection
    private Collection<String> urlsMedia;
    private Boolean isDeleted;
}
