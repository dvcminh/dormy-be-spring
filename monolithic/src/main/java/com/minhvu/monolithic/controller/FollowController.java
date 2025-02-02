package com.minhvu.monolithic.controller;


import com.minhvu.monolithic.dto.PendingFollowRequest;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/user/follow")
public class FollowController extends BaseController {
    @Autowired
    FollowService followService;

    @GetMapping("/check/{followingId}")
    public ResponseEntity<Boolean> checkIfUserFollows(@PathVariable UUID followingId) {
        AppUser currentUser = getCurrentUser();
        return followService.checkIfUserFollows(followingId, currentUser);
    }

    //api to follow user
    @PostMapping("/{userId}")
    private ResponseEntity<String> followUser(@PathVariable UUID userId) {
        AppUser userPrinciple = getCurrentUser();
        return followService.followUser(userId, userPrinciple);
    }

    //api to unfollow
    @DeleteMapping("/{userId}")
    private ResponseEntity<String> unfollow(@PathVariable UUID userId) {
        AppUser userPrinciple = getCurrentUser();
        return followService.unfollowUser(userId, userPrinciple);
    }

    //api to get all the pending following request of user
    @GetMapping("/pending-followers")
    private ResponseEntity<List<PendingFollowRequest>> getAllPendingRequest() {
        AppUser userPrinciple = getCurrentUser();
        return followService.getAllPendingRequest(userPrinciple);
    }

    //api to remove pending follow request
    @DeleteMapping("/pending/{userId}")
    private ResponseEntity<String> deletePendingRequest(@PathVariable UUID userId) {
        AppUser userPrinciple = getCurrentUser();
        return followService.deletePendingRequest(userId, userPrinciple);
    }


    //api to accept pending follow request
    @PutMapping("/requests/{requestId}/accept")
    private ResponseEntity<String> acceptPendingRequest(
            @PathVariable UUID requestId) {
        AppUser userPrinciple = getCurrentUser();
        return followService.acceptPendingRequest(requestId, userPrinciple);
    }

    //api to get all followers
    @GetMapping("/{userId}/followers")
    private ResponseEntity<List<PendingFollowRequest>> getAllFollowers(@PathVariable UUID userId) {
        AppUser userPrinciple = getCurrentUser();
        return followService.getAllFollowers(userId, userPrinciple);
    }

    //api to get all following
    @GetMapping("/{userid}/followings")
    private ResponseEntity<List<PendingFollowRequest>> getAllFollowing(@PathVariable UUID userid) {
        AppUser userPrinciple = getCurrentUser();
        return followService.getAllFollowing(userid, userPrinciple);
    }


    //api to get follower count
    @GetMapping("/{userId}/followers/count")
    private ResponseEntity<Long> FollowCount(@PathVariable UUID userId) {
        return followService.getAllFollowersCount(userId);
    }

    @GetMapping("/{userId}/following/count")
    private ResponseEntity<Long> followingCount(@PathVariable UUID userId) {
        return followService.getAllFollowingCount(userId);
    }

    @GetMapping("/{userId}/mutual-followers")
    private ResponseEntity<List<PendingFollowRequest>> mutualFollower(@PathVariable UUID userId){
        AppUser userPrinciple = getCurrentUser();
        return followService.mutualFollowers(userId,userPrinciple);
    }
}
