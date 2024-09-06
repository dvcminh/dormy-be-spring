package com.minhvu.interaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
public class Shared extends BaseEntity{
    private UUID postId;
    private UUID userId;
    private String sharedText;
    private boolean isDelete = Boolean.FALSE;
}
