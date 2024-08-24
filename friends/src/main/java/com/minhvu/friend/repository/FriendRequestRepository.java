package com.minhvu.friend.repository;

import com.minhvu.friend.model.entities.FriendRequest;
import com.minhvu.friend.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {

    List<FriendRequest> findByUserIdSender(UUID userIdSender);
    List<FriendRequest> findByUserIdSenderAndStatus(UUID userIdSender, Status status);
    List<FriendRequest> findByFriendId(UUID friendId);
    boolean existsByUserIdSenderAndFriendId(UUID userIdSender, UUID friendId);

    FriendRequest findByUserIdSenderAndFriendId(UUID userIdSender, UUID friendId);
}
