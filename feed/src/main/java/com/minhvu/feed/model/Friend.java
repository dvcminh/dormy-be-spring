package com.minhvu.feed.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
        acceptedAt = Date.from(LocalDateTime.now().toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }


}
