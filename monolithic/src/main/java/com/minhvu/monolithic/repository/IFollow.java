package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.Follow;
import com.minhvu.monolithic.entity.User;
import com.minhvu.monolithic.enums.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IFollow extends JpaRepository<Follow,Long> {

    Optional<Follow> findByFollowerAndFollowing(User user, User user1);

    List<Follow> findByFollowingAndStatus(User currentUser, FollowStatus followStatus);

    List<Follow> findByFollowing(User currentUser);

    List<Follow> findByFollower(User user);

    Optional<Follow> findByIdAndStatus(Long requestId, FollowStatus followStatus);

    List<Follow> findByFollowerAndStatus(User user, FollowStatus followStatus);

    Optional<Follow> findByFollowerAndFollowingAndStatus(User user, User user1, FollowStatus followStatus);

    Optional<Long> countByFollowingAndStatus(User user, FollowStatus followStatus);

    Optional<Long> countByFollowerAndStatus(User user, FollowStatus followStatus);
}
