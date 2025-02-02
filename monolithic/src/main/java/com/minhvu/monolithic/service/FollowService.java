package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.model.NotificationDto;
import com.minhvu.monolithic.dto.request.PendingFollowRequest;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Follow;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.enums.FollowStatus;
import com.minhvu.monolithic.repository.FollowRepository;
import com.minhvu.monolithic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ResponseEntity<String> checkIfUserFollows(UUID followingId, AppUser currentUser) {
        Optional<AppUser> followingUser = userRepository.findById(followingId);

        if (followingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Follow existingFollow = followRepository.findByFollowerAndFollowing(followingUser.get(), currentUser).orElseThrow(() -> new RuntimeException("Follow not found"));

        return ResponseEntity.status(HttpStatus.OK).body(existingFollow.getStatus().toString());
    }

    public ResponseEntity<String> followUser(UUID userId, AppUser userPrinciple) {

        // Check if the target user (to be followed) exists
        Optional<AppUser> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Check if the current user already follows the target user
        Optional<Follow> alreadyFollow = followRepository.findByFollowerAndFollowing(userPrinciple, targetUser.get());
        if (alreadyFollow.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already followed");
        }

        // Check if the target user is the same as the current user
        if (targetUser.get().getId().equals(userPrinciple.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You can't follow yourself");
        }

        Follow follow = new Follow();
        follow.setFollowing(targetUser.get());
        follow.setFollower(userPrinciple);
        NotificationDto notificationDto = new NotificationDto();

        // Set follow status based on account type

        if (targetUser.get().getAccountType().equals(AccountType.PUBLIC)) {
            follow.setStatus(FollowStatus.ACCEPTED);
            notificationDto = notificationService.generateNotification(targetUser.get().getId(), "New follower",
                    userPrinciple.getUsername() + " has follow you", "follow", userPrinciple.getId(), userPrinciple.getId());
        } else if (targetUser.get().getAccountType().equals(AccountType.PRIVATE)) {
            follow.setStatus(FollowStatus.PENDING);
            notificationDto = notificationService.generateNotification(targetUser.get().getId(), "New follow request",
                    userPrinciple.getUsername() + " has requested to follow you", "follow", userPrinciple.getId(), userPrinciple.getId());
        }

        try {
            followRepository.saveAndFlush(follow);
            notificationService.saveNotification(notificationDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Something went wrong,please try again letter");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("User followed successfully");
    }

    public ResponseEntity<String> unfollowUser(UUID userId, AppUser userPrinciple) {

        // Check if the target user ( followed) exists
        Optional<AppUser> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user you want to unfollow is not found");
        }

        // Check if the current user already follows the target user
        Optional<Follow> alreadyFollow = followRepository.findByFollowerAndFollowing(userPrinciple, targetUser.get());
        if (alreadyFollow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body("You have successfully unfollowed " + targetUser.get().getUsername());
        }

        try {
            followRepository.delete(alreadyFollow.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Something went wrong,please try again letter");
        }

        return ResponseEntity.status(HttpStatus.OK).body("User unfollowed successfully");

    }

    public ResponseEntity<List<PendingFollowRequest>> getAllPendingRequest(AppUser userPrinciple) {

        List<Follow> pendingRequest = followRepository.findByFollowingAndStatus(userPrinciple, FollowStatus.PENDING);

        // Map the Follow entities to PendingFollowRequest DTOs
        List<PendingFollowRequest> pendingFollowRequests = pendingRequest.stream().map(follow ->
                new PendingFollowRequest(
                        follow.getFollower().getId(),
                        follow.getFollower().getUsername(),
                        follow.getFollower().getDisplayName(),
                        follow.getFollower().getBio()
                )
        ).toList();

        // Return the mapped DTOs
        return ResponseEntity.status(HttpStatus.OK).body(pendingFollowRequests);
    }

    public ResponseEntity<String> deletePendingRequest(UUID userId, AppUser userPrinciple) {

        // Check if the target user (the user who sent the request) exists
        Optional<AppUser> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Target user not found");
        }

        // Check if there is a pending follow request where currentUser is the following and targetUser is the follower
        Optional<Follow> pendingFollowRequest = followRepository.findByFollowerAndFollowing(targetUser.get(), userPrinciple);
        if (pendingFollowRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No pending follow request found");
        }

        // Delete the pending follow request
        try {
            followRepository.delete(pendingFollowRequest.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong, please try again later");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Pending follow request deleted successfully");
    }

    public ResponseEntity<List<PendingFollowRequest>> getAllFollowers(UUID userId, AppUser userPrinciple) {

        Optional<AppUser> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        // Check if the account is private and the current user doesn't follow the target user
        boolean isPrivateAccount = user.get().getAccountType().equals(AccountType.PRIVATE);

        Optional<Follow> checkFollowing = followRepository.findByFollowerAndFollowingAndStatus(userPrinciple, user.get()
                , FollowStatus.ACCEPTED);

        if (isPrivateAccount && checkFollowing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }


        List<Follow> allFollowers = followRepository.findByFollowingAndStatus(user.get(), FollowStatus.ACCEPTED);

        //Map the Follow entities to PendingFollowRequest DTOs
        List<PendingFollowRequest> followers = allFollowers.stream().map(follow ->
                new PendingFollowRequest(
                        follow.getFollower().getId(),
                        follow.getFollower().getUsername(),
                        follow.getFollower().getDisplayName(),
                        follow.getFollower().getBio()
                )).toList();

        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

    public ResponseEntity<List<PendingFollowRequest>> getAllFollowing(UUID userId, AppUser userPrinciple) {
        Optional<AppUser> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }


        // Check if the account is private and the current user doesn't follow the target user
        boolean isPrivateAccount = user.get().getAccountType().equals(AccountType.PRIVATE);

        Optional<Follow> checkFollowing = followRepository.findByFollowerAndFollowingAndStatus(userPrinciple, user.get()
                , FollowStatus.ACCEPTED);

        if (isPrivateAccount && checkFollowing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }

        List<Follow> allFollowing = followRepository.findByFollowerAndStatus(user.get(), FollowStatus.ACCEPTED);

        List<PendingFollowRequest> following = allFollowing.stream().map(follow ->
                new PendingFollowRequest(
                        follow.getFollowing().getId(),
                        follow.getFollowing().getUsername(),
                        follow.getFollowing().getDisplayName(),
                        follow.getFollowing().getBio()
                )).toList();

        return ResponseEntity.status(HttpStatus.OK).body(following);


    }

    public ResponseEntity<String> acceptPendingRequest(UUID requestId, AppUser userPrinciple) {

        //getting follow request
        Optional<Follow> followRequest = followRepository.findByIdAndStatus(requestId, FollowStatus.PENDING);

        if (followRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No request found or request already accepted");
        }

        Follow request = followRequest.get();

        // Verify only user can accept the request for whom it made
        if (!request.getFollowing().equals(userPrinciple)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to accept this follow request");
        }

        request.setStatus(FollowStatus.ACCEPTED);

        try {
            followRepository.save(request);
            NotificationDto notificationDto = notificationService.generateNotification(request.getFollower().getId(), "Follow request accepted",
                    userPrinciple.getUsername() + " has accepted your follow request",
                    "follow",
                    followRequest.get().getFollower().getId(),
                    userPrinciple.getId());
            notificationService.saveNotification(notificationDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong please try again latter");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Follow request accepted successfully");

    }

    public ResponseEntity<Long> getAllFollowersCount(UUID userId) {
        Optional<AppUser> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<Long> count = followRepository.countByFollowingAndStatus(targetUser.get(), FollowStatus.ACCEPTED);

        if (count.isEmpty()){
            throw new RuntimeException("No followers found");
        }
        return ResponseEntity.ok(0L);
    }

    public ResponseEntity<Long> getAllFollowingCount(UUID userId) {
        Optional<AppUser> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }


        Optional<Long> followingCount = followRepository.countByFollowerAndStatus(targetUser.get(), FollowStatus.ACCEPTED);

        if (followingCount.isEmpty()){
            throw new RuntimeException("No following found");
        }
        return ResponseEntity.ok(followingCount.get());
    }

    public ResponseEntity<List<PendingFollowRequest>> mutualFollowers(UUID userId, AppUser userPrinciple) {
        // Find the target user by ID
        Optional<AppUser> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        // Get the list of users the current user is following
        List<Follow> currentUserFollowing = followRepository.findByFollowerAndStatus(userPrinciple, FollowStatus.ACCEPTED);

        // Get the list of users who are following the target user
        List<Follow> targetUserFollowers = followRepository.findByFollowingAndStatus(targetUser.get(), FollowStatus.ACCEPTED);

        // Extract mutual followers
        Set<UUID> currentUserFollowingIds = currentUserFollowing.stream()
                .map(follow -> follow.getFollowing().getId())
                .collect(Collectors.toSet());

        List<PendingFollowRequest> mutualFollowers = targetUserFollowers.stream()
                .filter(follow -> currentUserFollowingIds.contains(follow.getFollower().getId()))
                .map(follow -> new PendingFollowRequest(
                        follow.getFollower().getId(),
                        follow.getFollower().getUsername(),
                        follow.getFollower().getDisplayName(),
                        follow.getFollower().getBio()
                ))
                .toList();

        // Return the list of mutual followers
        return ResponseEntity.status(HttpStatus.OK).body(mutualFollowers);
    }
}
