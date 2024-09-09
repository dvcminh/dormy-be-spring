package com.minhvu.feed.repository;

import com.minhvu.feed.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    List<PostEntity> findPostEntitiesByUserId(UUID id);
}
