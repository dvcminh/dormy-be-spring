package com.minhvu.monolithic.service;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.ChatList;
import com.minhvu.monolithic.entity.Message;
import com.minhvu.monolithic.entity.enums.Status;
import com.minhvu.monolithic.repository.AppUserRepository;
import com.minhvu.monolithic.repository.ChatListRepository;
import com.minhvu.monolithic.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final AppUserRepository appUserRepository;
    private final ChatListRepository chatListRepository;


public Message saveMessage(Message message) {
    message.setDate(LocalDateTime.now().toString());

    // Get sender
    AppUser sender = appUserRepository.findByUsername(message.getSenderName())
            .orElseThrow(() -> new RuntimeException("Sender not found"));

    // Check if this is a group message by trying to parse receiverName as UUID
    try {
        UUID groupId = UUID.fromString(message.getReceiverName());
        // This is a group message, just save it without updating chat list
        return messageRepository.save(message);
    } catch (IllegalArgumentException e) {
        // This is a private message
        AppUser receiver = appUserRepository.findByUsername(message.getReceiverName())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Update chat lists for private messages
        updateChatList(sender, receiver, message.getMessage(), false);
        updateChatList(receiver, sender, message.getMessage(), true);

        return messageRepository.save(message);
    }
}

    private void updateChatList(AppUser user, AppUser contact, String message, boolean incrementUnread) {
        Optional<ChatList> chatListOpt = chatListRepository.findByUserAndContact(user, contact);

        if (chatListOpt.isPresent()) {
            ChatList chatList = chatListOpt.get();
            chatList.setLastMessage(message);
            chatList.setLastMessageTime(LocalDateTime.now());
            if (incrementUnread) {
                chatList.setUnreadCount(chatList.getUnreadCount() + 1);
            }
            chatListRepository.save(chatList);
        } else {
            ChatList newChatList = ChatList.builder()
                    .user(user)
                    .contact(contact)
                    .lastMessage(message)
                    .lastMessageTime(LocalDateTime.now())
                    .unreadCount(incrementUnread ? 1 : 0)
                    .build();
            chatListRepository.save(newChatList);
        }
    }

    public List<ChatList> getUserChatList(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return chatListRepository.findByUserOrderByLastMessageTimeDesc(user);
    }

    @Transactional
    public void markMessagesAsRead(String userName, String contactName) {
        AppUser user = appUserRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUser contact = appUserRepository.findByUsername(contactName)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        chatListRepository.findByUserAndContact(user, contact).ifPresent(chatList -> {
            chatList.setUnreadCount(0);
            chatListRepository.save(chatList);
        });
    }

    public List<Message> loadPrivateMessages(String user1, String user2) {
        List<Message> messagesUser1ToUser2 = messageRepository.findBySenderNameAndReceiverNameAndStatusAllIgnoreCaseOrderByDateAsc(user1, user2, Status.MESSAGE);
        List<Message> messagesUser2ToUser1 = messageRepository.findBySenderNameAndReceiverNameAndStatusAllIgnoreCaseOrderByDateAsc(user2, user1, Status.MESSAGE);

        messagesUser1ToUser2.addAll(messagesUser2ToUser1);
        messagesUser1ToUser2.sort(Comparator.comparing(Message::getDate));

        return messagesUser1ToUser2;
    }


    public List<Message> getGroupChatMessages(UUID groupName) {
        return messageRepository.findByReceiverNameAndStatusAllIgnoreCaseOrderByDateAsc(String.valueOf(groupName), Status.MESSAGE);
    }
}