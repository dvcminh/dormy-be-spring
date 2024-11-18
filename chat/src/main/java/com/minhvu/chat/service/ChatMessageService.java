package com.minhvu.chat.service;

import com.minhvu.chat.exception.ResourceNotFoundException;
import com.minhvu.chat.model.ChatMessage;
import com.minhvu.chat.model.MessageStatus;
import com.minhvu.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository repository;
    @Autowired
    private ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(UUID senderId, UUID recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(UUID senderId, UUID recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if (messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(UUID id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(UUID senderId, UUID recipientId, MessageStatus status) {
        List<ChatMessage> chatMessage = repository.findBySenderIdAndRecipientIdAndStatusAllIgnoreCaseOrderByTimestampAsc(senderId, recipientId, MessageStatus.RECEIVED);
        chatMessage.forEach(m -> {
            m.setStatus(status);
            repository.save(m);
        });
    }
}