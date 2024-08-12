package com.minhvu.friend.service;

import com.minhvu.friend.dto.FriendDto;
import com.minhvu.friend.dto.UserDTO;
import com.minhvu.friend.openfeign.UserClient;
import com.minhvu.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;
    @Qualifier
    private final UserClient userClient;

    public FriendDto findFriendIdsByUserId(Long userId)
    {

        List<Long> friendIds = friendRepository.findFriendIdsByUserId(userId);
        return FriendDto.builder()
                .userId(userId)
                .friendId(friendIds)
                .build();
    }

    public List<UserDTO> getAllFriendsProfile(Long userId)
    {
        List<Long> friendIds = friendRepository.findFriendIdsByUserId(userId);
        List<UserDTO> userDTOs = new ArrayList<>();
        friendIds.forEach(friendId -> {
            userDTOs.add(userClient.getUserById(friendId).getBody());
        });
        return userDTOs;
    }

    @Transactional
    public void deleteFriend(Long userId, Long friendId)
    {
        log.info("user with id {}  friend with id {} ", userId, friendId);
      friendRepository.deleteByUserIdAndFriendId(userId, friendId);
      log.info("Friend with id {} ", findFriendByUserId(userId, friendId));
      friendRepository.deleteByUserIdAndFriendId(friendId, userId);
        log.info("Friend with id {} ", findFriendByUserId(friendId, userId));
    }

    public Boolean existsByUserIdAndFriendId(Long userId, Long friendId) {
        return friendRepository.existsByUserIdAndFriendId(userId, friendId);
    }

    public Long countByUserId(Long userId) {
        return friendRepository.countByUserId(userId);
    }
    public Long findFriendByUserId(Long userId, Long friendId) {
        return friendRepository.findFriendIdByUserIdAndFriendId(userId, friendId);
    }


}
