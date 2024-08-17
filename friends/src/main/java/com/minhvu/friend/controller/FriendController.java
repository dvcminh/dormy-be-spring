package com.minhvu.friend.controller;

import com.minhvu.friend.dto.FriendDto;
import com.minhvu.friend.dto.UserDTO;
import com.minhvu.friend.service.FriendService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FriendController {

    private final FriendService friendService;


    @GetMapping("/profiles/all/{id}")
    public ResponseEntity<List<UserDTO>> getAllFriendsProfile(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(friendService.getAllFriendsProfile(userId));
    }


    // Get all friends
    @GetMapping("/user/all/{id}")
    public ResponseEntity<FriendDto> getFriends(@PathVariable("id") String userId) {
        return ResponseEntity.ok(friendService.findFriendIdsByUserId(Long.valueOf(userId)));
    }

    // Get  friend by id
    @GetMapping("/user/{id}")
    public ResponseEntity<Long> getFriend(@RequestHeader("id") String userId, @PathVariable Long friendId) {

        return ResponseEntity.ok(friendService.findFriendByUserId(Long.valueOf(userId), friendId));
    }
    // Delete a friend

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(@RequestHeader("id") String userId,@PathVariable Long friendId) {
        friendService.deleteFriend(Long.valueOf(userId), friendId);
        return ResponseEntity.noContent().build();
    }
}
