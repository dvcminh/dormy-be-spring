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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "chat_list")
public class ChatList extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user; // The user who owns the chat list

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private AppUser contact; // The other user in the chat

    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer unreadCount;
}
