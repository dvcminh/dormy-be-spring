package com.minhvu.friend.service;

import com.minhvu.friend.dto.FriendRequestDto;
import com.minhvu.friend.exception.FriendRequestException;
import com.minhvu.friend.exception.UserNotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRequestService {
    private final ModelMapper modelMapper;

    private final FriendRequestRepository friendRequestRepository;

    private final FriendRepository friendRepository;
    @Qualifier("com.minhvu.friend.openfeign.UserClient")
    private final UserClient userClient;



    public FriendRequestDto createFriendRequest(Long userIdSender, Long friendId) {
        log.info("Creating friend request for user {} and friend {}", userIdSender, friendId);
        if(!userClient.userExists(friendId)) throwUserNotFoundException(friendId);

        if(friendRepository.existsByUserIdAndFriendId(userIdSender, friendId))  throwFriendRequestException("Vous êtes déjà amis");
        checkIfUserIsSendingRequestToSelf(userIdSender, friendId);
        checkFriendRequestStatusAndThrowException(getFriendRequest(userIdSender, friendId));
        if(friendRequestRepository.findByFriendIdAndUserIdSender(userIdSender, friendId).isPresent()) throwFriendRequestException("Demande d'amis déjà envoyée");
        FriendRequest friendRequest = FriendRequest.builder()
                .userIdSender(userIdSender)
                .friendId(friendId)
                .createdAt(LocalDateTime.now())
                .status(Status.PENDING)
                .updatedAt(LocalDateTime.now())
                .build();
        //TODO Send Notification to the user
        return modelMapper.map(friendRequestRepository.save(friendRequest), FriendRequestDto.class);
    }

    public Optional<FriendRequest> getFriendRequest(Long userId, Long requestId) {
        log.info("Getting friend request for user {} and friend {}", userId, requestId);
        return friendRequestRepository.findByFriendIdAndId(userId, requestId);
    }


    @Transactional
    public FriendRequestDto acceptFriendRequest(Long userId, Long requestId) {
        log.info("Accepting friend request with id {}", requestId);

        Optional<FriendRequest> friendRequest = getFriendRequest(userId, requestId);
        log.info("Optional Status friend request with id {}", friendRequest.isPresent());
        if (!friendRequest.isPresent()) {
         throwFriendRequestException("Friend request does not exist");
        }

        if(!userClient.userExists(friendRequest.get().getUserIdSender())) throwUserNotFoundException(friendRequest.get().getFriendId());
        log.info("UserClien Status friend request with id {}", requestId);

        if(friendRepository.existsByUserIdAndFriendId(userId, friendRequest.get().getUserIdSender()))  throwFriendRequestException("Friend already exists");
        log.info("FriendRepository Status friend request with id {}", requestId);
        if(!Objects.equals(userId, friendRequest.get().getFriendId())) throwFriendRequestException("You can't accept a friend request that is not for you");
        friendRequest.get().setStatus(Status.ACCEPTED);
        friendRequest.get().setUpdatedAt(LocalDateTime.now());
        Friend friend = Friend.builder()
                .userId(userId)
                .friendId(friendRequest.get().getUserIdSender())
                .build();
        Friend friend1 = Friend.builder()
                .userId(friendRequest.get().getUserIdSender())
                .friendId(userId)
                .build();
        friendRepository.save(friend);
        friendRepository.save(friend1);
        friendRequestRepository.save(friendRequest.get());

        return modelMapper.map(friendRequest.get(), FriendRequestDto.class);
    }
    public List<FriendRequestDto> getAllFriendRequestByIdReceiver(Long idReceiver) {
        log.info("Getting all friend request for user with id {}", idReceiver);
        return friendRequestRepository.findByFriendId(idReceiver).stream().map(element -> modelMapper.map(element, FriendRequestDto.class)).toList();
    }

    public FriendRequestDto rejectFriendRequest(Long userId, Long requestId) {
        log.info("Rejecting friend request with id {}", requestId);

        Optional<FriendRequest> friendRequest = friendRequestRepository.findById(requestId);
        if (!friendRequest.isPresent()) {
            throwFriendRequestException("Friend request does not exist");
        }
        if(!userClient.userExists(friendRequest.get().getFriendId())) throwUserNotFoundException(friendRequest.get().getFriendId());

        Boolean isFriend = friendRepository.existsByUserIdAndFriendId(userId, friendRequest.get().getFriendId());
        if(isFriend){
            throwFriendRequestException("You are already friends");
        }
        if(!Objects.equals(userId, friendRequest.get().getFriendId())) throwFriendRequestException("You can't reject a friend request that is not for you");

        friendRequest.get().setStatus(Status.REJECTED);
        friendRequest.get().setUpdatedAt(LocalDateTime.now());
        return  modelMapper.map(friendRequestRepository.save(friendRequest.get()), FriendRequestDto.class);
    }

    public List<FriendRequestDto> getAllFriendRequestByIdSender(Long idSender) {
        log.info("Getting all friend request for user with id {}", idSender);
         return friendRequestRepository.findByUserIdSender(idSender).stream().map(element -> modelMapper.map(element, FriendRequestDto.class)).toList();
    }

    public List<FriendRequestDto> getAllFriendRequestByIdSenderAndStatus(Long idSender, Status status) {
        log.info("Getting all friend request for user with id {} and status {}", idSender, status);
        return friendRequestRepository.findByUserIdSenderAndStatus(idSender, status).stream().map(element -> modelMapper.map(element, FriendRequestDto.class)).toList();
    }



    //TODO complet delete friend and request
    public void checkFriendRequestStatusAndThrowException(Optional<FriendRequest> request) {
        if(request.isPresent()){
            switch (request.get().getStatus()) {
                case ACCEPTED:
                    throwFriendRequestException("Vous êtes déjà amis");
                    break;
                case PENDING:
                    throwFriendRequestException("Demande d'amis déjà envoyée");
                    break;
                case REJECTED:
                    throwFriendRequestException("Demande d'amis Rejected");
                    break;
                default:
                    throwFriendRequestException("Demande d'amis n'existe pas");
                    break;
            }
        }
    }
    private void throwFriendRequestException(String message) {
        throw new FriendRequestException(message);
    }
    private void throwUserNotFoundException(Long friendId) {
        throw new UserNotFoundException(String.format("User with id %s does not exist", friendId));
    }



    public void checkIfUserIsSendingRequestToSelf(Long loggedInUserId, Long senderId) {
        log.info("Checking if user is sending request to self {} {}", loggedInUserId, senderId);
        if (loggedInUserId.equals(senderId)) {
            throwFriendRequestException("You can't send a friend request to yourself");
        }
    }

    public void deleteFriendRequest(long l, Long requestId) {
        log.info("Deleting friend request with id {}", requestId);
        if(!friendRequestRepository.existsById(requestId)) throwFriendRequestException("Friend request does not exist");
        if(!friendRequestRepository.findById(requestId).get().getUserIdSender().equals(l)) throwFriendRequestException("You can't delete a friend request that is not yours");
        friendRequestRepository.deleteById(requestId);
    }
}
