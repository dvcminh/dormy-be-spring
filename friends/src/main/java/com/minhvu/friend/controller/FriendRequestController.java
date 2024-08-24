package com.minhvu.friend.controller;


import com.minhvu.friend.dto.AppUserDto;
import com.minhvu.friend.dto.FriendRequestDto;
import com.minhvu.friend.model.entities.Friend;
import com.minhvu.friend.model.entities.FriendRequest;
import com.minhvu.friend.model.enums.Status;
import com.minhvu.friend.service.FriendRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friends/requests")
@CrossOrigin("*")
public class FriendRequestController extends BaseController{

    @Autowired
    private FriendRequestService friendRequestService;

    @GetMapping("/send")
    public ResponseEntity<List<FriendRequestDto>> getAllFriendRequest(@RequestHeader("id") UUID userId) {
        return ResponseEntity.ok(friendRequestService.getAllFriendRequestByIdSender(userId));

    }

    @GetMapping("/received")
    public ResponseEntity<List<FriendRequestDto>> getAllFriendRequestReceived(@RequestHeader("id") UUID userId) {
        return ResponseEntity.ok(friendRequestService.getAllFriendRequestByIdReceiver(userId));
    }

    @GetMapping("/all/status")
    public ResponseEntity<List<FriendRequestDto>> getAllFriendRequestByStatus(@RequestHeader("id") UUID userId, @RequestParam("status") String status) {
        Status statusEnum = Status.fromString(status);
        return ResponseEntity.ok(friendRequestService.getAllFriendRequestByIdSenderAndStatus(userId, statusEnum));
    }

    @PostMapping("/user/{friendId}")
    public ResponseEntity<FriendRequestDto> createFriendRequest(@PathVariable("friendId") UUID friendId, HttpServletRequest request) {
        AppUserDto user = getCurrentUser(request);
        return ResponseEntity.ok(friendRequestService.createFriendRequest(user.getId(), friendId));
    }

    @PutMapping("/accept/{friendId}")
    public ResponseEntity<FriendRequestDto> acceptFriendRequest(@PathVariable("friendId") UUID friendId, HttpServletRequest request) {
        AppUserDto user = getCurrentUser(request);
        return ResponseEntity.ok(friendRequestService.acceptFriendRequest(user.getId(), friendId));
    }

    @PutMapping("/reject/{friendId}")
    public ResponseEntity<FriendRequestDto> rejectFriendRequest(@PathVariable("friendId") UUID friendId, HttpServletRequest request) {
        AppUserDto user = getCurrentUser(request);
        return ResponseEntity.ok(friendRequestService.rejectFriendRequest(user.getId(), friendId));
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteFriendRequest(@RequestHeader("id") UUID userId, @PathVariable UUID requestId) {
        friendRequestService.deleteFriendRequest(userId, requestId);
        return ResponseEntity.noContent().build();
    }


}

