package com.minhvu.friend.controller;

import com.minhvu.friend.dto.AppUserDto;
import com.minhvu.friend.dto.UserFriendDto;
import com.minhvu.friend.dto.UserDTO;
import com.minhvu.friend.exception.response.Response;
import com.minhvu.friend.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friends/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FriendController extends BaseController{

    private final FriendService friendService;


    @GetMapping("/profiles/all/{id}")
    public ResponseEntity<List<UserDTO>> getAllFriendsProfile(@PathVariable("id") UUID userId) {

        return ResponseEntity.ok(friendService.getAllFriendsProfile(userId));
    }


    // Get all friends
    @GetMapping("/user/all/{id}")
    public ResponseEntity<UserFriendDto> getFriends(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok(friendService.findFriendIdsByUserId(userId));
    }

    // Get  friend by id
    @GetMapping("/user/{id}")
    public ResponseEntity<UUID> getFriend(@RequestHeader("id") UUID userId, @PathVariable UUID friendId) {

        return ResponseEntity.ok(friendService.findFriendByUserId(userId, friendId));
    }
    // Delete a friend

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend( @PathVariable UUID friendId, HttpServletRequest request) {
        AppUserDto currentUser = getCurrentUser(request);
        UUID userId = currentUser.getId();
        friendService.deleteFriend(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sync")
    public ResponseEntity<Response> syncFriend() {
        return ResponseEntity.ok(new Response(friendService.syncFriend()));
    }

}
