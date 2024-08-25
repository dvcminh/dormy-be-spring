package com.minhvu.friend.model.entities;


import com.minhvu.friend.model.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reactions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE reactions SET is_delete = true WHERE id=?")
@Where(clause = "is_delete = false")
public class Reaction extends BaseEntity{
    
    private UUID postId;
    private UUID userId;
    private ReactionType reactionType;
    private boolean isDelete = Boolean.FALSE;
}
