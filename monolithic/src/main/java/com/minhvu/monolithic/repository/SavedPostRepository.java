package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.entity.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, UUID> {

    Optional<SavedPost> findByUserAndPost(AppUser AppUser, Post post);

    boolean existsByUserAndPost(AppUser AppUser, Post post);

    void deleteByUserAndPost(AppUser AppUser, Post post);

    List<SavedPost> findAllByUser(AppUser AppUser);
}
