package com.minhvu.friend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shareds")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE shareds SET is_delete = true WHERE id=?")
@Where(clause = "is_delete = false")
public class Shared extends BaseEntity{
    
    private UUID postId;
    private UUID userId;
    private boolean isDelete = Boolean.FALSE;
}
