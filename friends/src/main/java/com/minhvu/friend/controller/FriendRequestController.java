package com.minhvu.friend.controller;



import com.minhvu.friend.dto.FriendRequestDto;
import com.minhvu.friend.model.entities.Friend;
import com.minhvu.friend.model.entities.FriendRequest;
import com.minhvu.friend.model.enums.Status;
import com.minhvu.friend.service.FriendRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend/requests")
@Slf4j
@CrossOrigin("*")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;
    @GetMapping("/send")
    public ResponseEntity<List<FriendRequestDto>> getAllFriendRequest(@RequestHeader("id") String userId) {
        return ResponseEntity.ok(friendRequestService.getAllFriendRequestByIdSender(Long.parseLong(userId)));

    }
    @GetMapping("/received")
    public ResponseEntity<List<FriendRequestDto>> getAllFriendRequestReceived(@RequestHeader("id") String userId) {
        return ResponseEntity.ok(friendRequestService.getAllFriendRequestByIdReceiver(Long.parseLong(userId)));
    }

    @GetMapping("/all/status")
    public ResponseEntity<List<FriendRequestDto>> getAllFriendRequestByStatus(@RequestHeader("id") String userId, @RequestParam("status") String status) {
        return ResponseEntity.ok(friendRequestService.getAllFriendRequestByIdSenderAndStatus(Long.parseLong(userId), Status.valueOf(status)));
    }

    @PostMapping("/user/{friendId}")
    public ResponseEntity<FriendRequestDto> createFriendRequest(@RequestHeader("id") String userId, @PathVariable Long friendId    ) {
        return ResponseEntity.ok(friendRequestService.createFriendRequest(Long.parseLong(userId), friendId));
    }

    @PutMapping("/{requestId}/accept")
    public ResponseEntity<FriendRequestDto> acceptFriendRequest(@RequestHeader("id") String userId, @PathVariable Long requestId) {
       return ResponseEntity.ok(friendRequestService.acceptFriendRequest(Long.parseLong(userId),requestId));
    }

    @PutMapping("/{requestId}/reject")
    public ResponseEntity<FriendRequestDto> rejectFriendRequest(@RequestHeader("id") String userId, @PathVariable Long requestId) {
        log.info("Rejecting friend request with id {}", requestId);
        return ResponseEntity.ok(friendRequestService.rejectFriendRequest(Long.parseLong(userId),requestId));
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteFriendRequest(@RequestHeader("id") String userId, @PathVariable Long requestId) {
        friendRequestService.deleteFriendRequest(Long.parseLong(userId), requestId);
        return ResponseEntity.noContent().build();
    }


}

