package com.minhvu.interaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Data
@Table(name = "comments")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE comments SET is_delete = true WHERE id=?")
@Where(clause = "is_delete = false")
public class Comment extends BaseEntity {
    
    private UUID postId;
    private UUID userId;
    private String commentText;
    private boolean isDelete = Boolean.FALSE;
}
