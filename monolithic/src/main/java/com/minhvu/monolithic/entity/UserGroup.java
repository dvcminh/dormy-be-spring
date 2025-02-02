package com.minhvu.monolithic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "group_chat_id")
    private GroupChat groupChat;
    private LocalDateTime joinedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}