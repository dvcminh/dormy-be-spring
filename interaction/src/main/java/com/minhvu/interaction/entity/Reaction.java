package com.minhvu.interaction.entity;


import com.minhvu.interaction.entity.enums.ReactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "reactions")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE reactions SET is_delete = true WHERE id=?")
@Where(clause = "is_delete = false")
public class Reaction extends BaseEntity{
    
    private UUID postId;
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    private boolean isDelete = Boolean.FALSE;
}
