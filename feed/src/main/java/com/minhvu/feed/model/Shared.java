package com.minhvu.feed.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "shareds")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE shareds SET is_delete = true WHERE id=?")
@Where(clause = "is_delete = false")
public class Shared{
    @Id
    private UUID id;
    private UUID postId;
    private UUID userId;
    private String sharedText;
    private boolean isDelete = Boolean.FALSE;
    private Date createdAt;
    private Date updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
