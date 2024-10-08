package com.minhvu.friend.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "friends")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend extends BaseEntity {


    private UUID userId;
    private UUID friendId;
    @Column(name = "accepted_at")
    private Date acceptedAt;


    @PrePersist
    protected void onCreate() {
        acceptedAt = Date.from(LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC));
    }

}
