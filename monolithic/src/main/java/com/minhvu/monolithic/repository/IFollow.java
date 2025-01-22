package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Follow;
import com.minhvu.monolithic.enums.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IFollow extends JpaRepository<Follow, UUID> {

    Optional<Follow> findByFollowerAndFollowing(AppUser AppUser, AppUser AppUser1);

    List<Follow> findByFollowingAndStatus(AppUser currentAppUser, FollowStatus followStatus);

    List<Follow> findByFollowing(AppUser currentAppUser);

    List<Follow> findByFollower(AppUser AppUser);

    Optional<Follow> findByIdAndStatus(UUID requestId, FollowStatus followStatus);

    List<Follow> findByFollowerAndStatus(AppUser AppUser, FollowStatus followStatus);

    Optional<Follow> findByFollowerAndFollowingAndStatus(AppUser AppUser, AppUser AppUser1, FollowStatus followStatus);

    Optional<Long> countByFollowingAndStatus(AppUser AppUser, FollowStatus followStatus);

    Optional<Long> countByFollowerAndStatus(AppUser AppUser, FollowStatus followStatus);
}
