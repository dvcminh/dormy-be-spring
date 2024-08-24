package com.minhvu.friend.repository;


import com.minhvu.friend.model.entities.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, UUID> {

    @Query("SELECT f.friendId FROM Friend f WHERE f.userId = :userId")
    List<UUID> findFriendIdsByUserId(@Param("userId") UUID userId);
    void deleteByUserIdAndFriendId(UUID userId, UUID friendId);
    Boolean existsByUserIdAndFriendId(UUID userId, UUID friendId);
    UUID countByUserId(UUID userId);
    @Query("SELECT f.friendId FROM Friend f WHERE f.userId = :userId AND f.friendId = :friendId")
    UUID findFriendIdByUserIdAndFriendId(@Param("userId") UUID userId, @Param("friendId") UUID friendId);

}
