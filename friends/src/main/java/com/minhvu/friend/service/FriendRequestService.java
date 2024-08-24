package com.minhvu.friend.service;

import com.minhvu.friend.dto.FriendRequestDto;
import com.minhvu.friend.dto.mapper.FriendMapper;
import com.minhvu.friend.exception.FriendRequestException;
import com.minhvu.friend.exception.UserNotFoundException;
import com.minhvu.friend.kafka.FriendProducer;
import com.minhvu.friend.model.entities.Friend;
import com.minhvu.friend.model.entities.FriendRequest;
import com.minhvu.friend.model.enums.Status;
import com.minhvu.friend.openfeign.UserClient;
import com.minhvu.friend.repository.FriendRepository;
import com.minhvu.friend.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRequestService {
    private final ModelMapper modelMapper;

    private final FriendRequestRepository friendRequestRepository;

    private final FriendRepository friendRepository;
    private final UserService userService;

    private final FriendProducer friendProducer;
    private final FriendMapper friendMapper;

    public FriendRequestDto createFriendRequest(UUID userIdSender, UUID friendId) {
        log.info("Creating friend request for user {} and friend {}", userIdSender, friendId);
//        if(!userClient.userExists(friendId)) throw new RuntimeException(friendId);
        if(!userService.existsById(friendId)) throw new RuntimeException("error");

        if(friendRepository.existsByUserIdAndFriendId(userIdSender, friendId))  throw new RuntimeException("You are already friends");
        checkIfUserIsSendingRequestToSelf(userIdSender, friendId);
        if(friendRequestRepository.existsByUserIdSenderAndFriendId(userIdSender, friendId)) throw new RuntimeException("Friend request already exists");
        FriendRequest friendRequest = FriendRequest.builder()
                .userIdSender(userIdSender)
                .friendId(friendId)
                .status(Status.PENDING)
                .build();
        return modelMapper.map(friendRequestRepository.save(friendRequest), FriendRequestDto.class);
    }


    @Transactional
    public FriendRequestDto acceptFriendRequest(UUID userId, UUID requestId) {
        log.info("Accepting friend request with id {}", requestId);

        FriendRequest friendRequest = friendRequestRepository.findByUserIdSenderAndFriendId(userId, requestId);
        log.info("Optional Status friend request with id {}", friendRequest);
        if (friendRequest == null) {
            throw new RuntimeException("Friend request does not exist");
        }

//        if(!userClient.userExists(friendRequest.get().getUserIdSender())) throw new RuntimeException(friendRequest.get().getFriendId());
        if(userService.existsById(friendRequest.getUserIdSender())) throw new RuntimeException("error");
        log.info("UserClien Status friend request with id {}", requestId);

        if(friendRepository.existsByUserIdAndFriendId(userId, friendRequest.getUserIdSender()))  throw new RuntimeException("Friend already exists");
        log.info("FriendRepository Status friend request with id {}", requestId);
        if(!Objects.equals(userId, friendRequest.getFriendId())) throw new RuntimeException("You can't accept a friend request that is not for you");
        friendRequest.setStatus(Status.ACCEPTED);
        Friend friend = Friend.builder()
                .userId(userId)
                .friendId(friendRequest.getUserIdSender())
                .build();
        Friend friend1 = Friend.builder()
                .userId(friendRequest.getUserIdSender())
                .friendId(userId)
                .build();
        friendRepository.save(friend);
        friendRepository.save(friend1);
        friendProducer.sendMessage(friendMapper.toDto(friend));
        friendProducer.sendMessage(friendMapper.toDto(friend1));
        friendRequestRepository.save(friendRequest);

        return modelMapper.map(friendRequest, FriendRequestDto.class);
    }
    public List<FriendRequestDto> getAllFriendRequestByIdReceiver(UUID idReceiver) {
        log.info("Getting all friend request for user with id {}", idReceiver);
        return friendRequestRepository.findByFriendId(idReceiver).stream().map(element -> modelMapper.map(element, FriendRequestDto.class)).toList();
    }

    public FriendRequestDto rejectFriendRequest(UUID userId, UUID requestId) {
        log.info("Rejecting friend request with id {}", requestId);

        Optional<FriendRequest> friendRequest = friendRequestRepository.findById(requestId);
        if (!friendRequest.isPresent()) {
            throw new RuntimeException("Friend request does not exist");
        }
//        if(!userClient.userExists(friendRequest.get().getFriendId())) throw new RuntimeException(friendRequest.get().getFriendId());

        if(userService.existsById(friendRequest.get().getFriendId())) throw new RuntimeException("error");

        Boolean isFriend = friendRepository.existsByUserIdAndFriendId(userId, friendRequest.get().getFriendId());
        if(isFriend){
            throw new RuntimeException("You are already friends");
        }
        if(!Objects.equals(userId, friendRequest.get().getFriendId())) throw new RuntimeException("You can't reject a friend request that is not for you");

        friendRequest.get().setStatus(Status.REJECTED);
        return  modelMapper.map(friendRequestRepository.save(friendRequest.get()), FriendRequestDto.class);
    }

    public List<FriendRequestDto> getAllFriendRequestByIdSender(UUID idSender) {
        log.info("Getting all friend request for user with id {}", idSender);
         return friendRequestRepository.findByUserIdSender(idSender).stream().map(element -> modelMapper.map(element, FriendRequestDto.class)).toList();
    }

    public List<FriendRequestDto> getAllFriendRequestByIdSenderAndStatus(UUID idSender, Status status) {
        log.info("Getting all friend request for user with id {} and status {}", idSender, status);
        return friendRequestRepository.findByUserIdSenderAndStatus(idSender, status).stream().map(element -> modelMapper.map(element, FriendRequestDto.class)).toList();
    }



    public void checkIfUserIsSendingRequestToSelf(UUID loggedInUserId, UUID senderId) {
        log.info("Checking if user is sending request to self {} {}", loggedInUserId, senderId);
        if (loggedInUserId.equals(senderId)) {
            throw new RuntimeException("You can't send a friend request to yourself");
        }
    }

    public void deleteFriendRequest(UUID l, UUID requestId) {
        log.info("Deleting friend request with id {}", requestId);
        if(!friendRequestRepository.existsById(requestId)) throw new RuntimeException("Friend request does not exist");
        if(!friendRequestRepository.findById(requestId).get().getUserIdSender().equals(l)) throw new RuntimeException("You can't delete a friend request that is not yours");
        friendRequestRepository.deleteById(requestId);
    }
}
