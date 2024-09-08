package com.minhvu.feed.repository;


import com.minhvu.feed.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, UUID> {

    @Query("SELECT f.friendId FROM Friend f WHERE f.userId = :userId")
    List<UUID> findAllByUserId(@Param("userId") UUID userId);
}
