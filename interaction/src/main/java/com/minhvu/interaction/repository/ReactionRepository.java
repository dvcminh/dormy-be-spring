package com.minhvu.interaction.repository;

import com.minhvu.interaction.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, UUID> {

    List<Reaction> findByPostId(UUID postId);

    boolean existsByUserIdAndPostId(UUID userId, UUID postId);
}
