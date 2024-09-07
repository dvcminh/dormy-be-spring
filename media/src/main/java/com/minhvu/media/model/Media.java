package com.minhvu.media.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import  jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "medias")
@AllArgsConstructor
@NoArgsConstructor

public class Media extends BaseEntity {

    private String filename;
    private UUID userId;
    private String uri;
    private UUID postId;
    private String fileType;

}