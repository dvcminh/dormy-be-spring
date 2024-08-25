package com.minhvu.friend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "comments")
@Getter
@Setter
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
