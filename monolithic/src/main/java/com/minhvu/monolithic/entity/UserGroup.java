package com.minhvu.monolithic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_group")
public class UserGroup extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "group_chat_id")
    private GroupChat groupChat;
    private LocalDateTime joinedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}