package com.minhvu.feed.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "reactions")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Reaction{
    @Id
    private UUID id;
    private UUID postId;
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    private boolean isDelete = Boolean.FALSE;
    private Date createdAt;
    private Date updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
