package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByUser(AppUser user);

    Page<Post> findByCaptionIgnoreCaseContaining(String query, Pageable pageable);

    Page<Post> findByUserIn(List<AppUser> followedUser, Pageable pageable);

    List<Post> findByPostTypeAndUser_AccountType(PostType postType, AccountType accountType, Pageable pageable);

    List<Post> findByPostType(PostType postType,  Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId")
    long countByUserId(UUID userId);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId AND p.createdAt > :date")
    long countByUserIdAndCreatedAtAfter(UUID userId, LocalDateTime date);
}
