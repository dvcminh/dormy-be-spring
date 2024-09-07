package com.minhvu.feed.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "medias")
@AllArgsConstructor
@NoArgsConstructor

public class Media {
    @Id
    private UUID id;
    private UUID postId;
    private UUID userId;
    private String filename;
    private String uri;
    private String fileType;
    private Date createdAt;
}