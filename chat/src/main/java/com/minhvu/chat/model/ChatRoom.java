package com.minhvu.chat.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatRoom extends BaseEntity{
    private UUID chatId;
    private UUID senderId;
    private UUID recipientId;
}
