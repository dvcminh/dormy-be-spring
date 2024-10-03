package com.minhvu.review.repository;

import com.minhvu.review.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    List<PostEntity> findPostEntitiesByUserId(UUID id);

    Optional<PostEntity> findByIdAndUserId(UUID id, UUID userId);

}
