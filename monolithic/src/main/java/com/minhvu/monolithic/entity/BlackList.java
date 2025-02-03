package com.minhvu.monolithic.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_blocklist", uniqueConstraints = @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackList extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blocker_id", nullable = false)
    private AppUser blocker; // The user who initiated the block

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blocked_id", nullable = false)
    private AppUser blocked;// The user who was blocked

    @Column(name = "blocked_at", nullable = false, updatable = false)
    private LocalDateTime blockedAt;
}
