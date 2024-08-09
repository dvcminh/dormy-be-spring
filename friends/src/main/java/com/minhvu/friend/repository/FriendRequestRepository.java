package com.minhvu.friend.repository;

import com.minhvu.friend.model.entities.FriendRequest;
import com.minhvu.friend.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {

    Optional<FriendRequest> findByFriendIdAndId(Long userId, Long requestId);
    Optional<FriendRequest> findByFriendIdAndUserIdSender(Long friendId, Long userId);
    List<FriendRequest> findByUserIdSender(Long userIdSender);
    List<FriendRequest> findByUserIdSenderAndStatus(Long userIdSender, Status status);
    List<FriendRequest> findByFriendId(Long friendId);

}
