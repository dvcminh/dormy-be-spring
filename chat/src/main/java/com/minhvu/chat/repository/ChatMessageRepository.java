package com.minhvu.chat.repository;

import com.minhvu.chat.model.ChatMessage;
import com.minhvu.chat.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, UUID> {

    long countBySenderIdAndRecipientIdAndStatus(
            UUID senderId, UUID recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(UUID chatId);

    List<ChatMessage> findBySenderIdAndRecipientIdAndStatusAllIgnoreCaseOrderByTimestampAsc(UUID senderId, UUID recipientId, MessageStatus status);
}