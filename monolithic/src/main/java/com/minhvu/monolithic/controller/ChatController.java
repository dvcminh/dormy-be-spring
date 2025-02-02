package com.minhvu.monolithic.controller;

import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.dto.response.GroupChatDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.GroupChat;
import com.minhvu.monolithic.entity.Message;
import com.minhvu.monolithic.service.GroupChatService;
import com.minhvu.monolithic.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ChatController extends BaseController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final GroupChatService groupChatService;

    @PostMapping("/create-group-chat")
    public ResponseEntity<GroupChatDto> createGroupChat(@RequestBody List<UUID> userids) {
        return ResponseEntity.ok().body(groupChatService.createGroupChat("Group Chat", "https://www.google.com", userids));
    }

    @PostMapping("/add-user-to-group-chat")
    public ResponseEntity<String> addUserToGroupChat(@RequestParam UUID groupId, @RequestParam UUID userId) {
        groupChatService.addUserToGroupChat(groupId, userId);
        return ResponseEntity.ok().body("User added to group chat");
    }

    @PostMapping("/remove-user-from-group-chat")
    public ResponseEntity<String> removeUserFromGroupChat(@RequestParam UUID groupId, @RequestParam UUID userId) {
        groupChatService.removeUserFromGroupChat(groupId, userId);
        return ResponseEntity.ok().body("User removed from group chat");
    }

    @GetMapping("/group-chats")
    public ResponseEntity<List<GroupChat>> getGroupChats() {
        AppUser userPrinciple = getCurrentUser();
        return ResponseEntity.ok().body(groupChatService.getGroupChats(userPrinciple));
    }

    @GetMapping("/group-chat/{groupId}/users")
    public ResponseEntity<List<AppUserDto>> getGroupChatUsers(@PathVariable UUID groupId) {
        return ResponseEntity.ok().body(groupChatService.getGroupChatUsers(groupId));
    }

    @GetMapping("/group-chat/{groupId}/messages")
    public ResponseEntity<List<Message>> getGroupChatMessages(@PathVariable UUID groupId) {
        return ResponseEntity.ok().body(messageService.getGroupChatMessages(groupId));
    }

    @GetMapping("/private-messages/{senderName}/{receiverName}")
    public ResponseEntity<List<Message>> getPrivateMessages(@PathVariable String senderName, @PathVariable String receiverName) {
        List<Message> messages = messageService.loadPrivateMessages(senderName, receiverName);
        return ResponseEntity.ok(messages);
    }

    @MessageMapping("/group-message/{groupId}")
    @SendTo("/chatroom/group/{groupId}")
    public Message receiveGroupMessage(@DestinationVariable UUID groupId, @Payload Message message) {
        messageService.saveMessage(message);
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
        messageService.saveMessage(message);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
        System.out.println(message);
        return message;
    }
}
