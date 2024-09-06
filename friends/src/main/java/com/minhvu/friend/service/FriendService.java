package com.minhvu.friend.service;

import com.minhvu.friend.dto.UserFriendDto;
import com.minhvu.friend.dto.UserDTO;
import com.minhvu.friend.dto.mapper.FriendMapper;
import com.minhvu.friend.kafka.FriendProducer;
import com.minhvu.friend.openfeign.UserClient;
import com.minhvu.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;
    @Qualifier("com.minhvu.friend.openfeign.UserClient")
    private final UserClient userClient;

    private final FriendProducer friendProducer;
    private final FriendMapper friendMapper;

    public UserFriendDto findFriendIdsByUserId(UUID userId) {

        List<UUID> friendIds = friendRepository.findFriendIdsByUserId(userId);
        return UserFriendDto.builder()
                .userId(userId)
                .friendId(friendIds)
                .build();
    }

    public List<UserDTO> getAllFriendsProfile(UUID userId) {
        List<UUID> friendIds = friendRepository.findFriendIdsByUserId(userId);
        List<UserDTO> userDTOs = new ArrayList<>();
        friendIds.forEach(friendId -> {
            userDTOs.add(userClient.getUserById(friendId).getBody());
        });
        return userDTOs;
    }

    @Transactional
    public void deleteFriend(UUID userId, UUID friendId) {
        log.info("user with id {}  friend with id {} ", userId, friendId);
        friendRepository.deleteByUserIdAndFriendId(userId, friendId);
        log.info("Friend with id {} ", findFriendByUserId(userId, friendId));
        friendRepository.deleteByUserIdAndFriendId(friendId, userId);
        log.info("Friend with id {} ", findFriendByUserId(friendId, userId));
    }

    public Boolean existsByUserIdAndFriendId(UUID userId, UUID friendId) {
        return friendRepository.existsByUserIdAndFriendId(userId, friendId);
    }

    public UUID countByUserId(UUID userId) {
        return friendRepository.countByUserId(userId);
    }

    public UUID findFriendByUserId(UUID userId, UUID friendId) {
        return friendRepository.findFriendIdByUserIdAndFriendId(userId, friendId);
    }

    public String syncFriend() {
        friendRepository.findAll().forEach(appUser -> {
            friendProducer.sendMessage(friendMapper.toDto(appUser));
        });
        return "Sync users successfully";
    }
}
