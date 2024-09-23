package com.minhvu.interaction.repository;

import com.minhvu.interaction.entity.Shared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SharedRepository extends JpaRepository<Shared, UUID> {
    List<Shared> findByPostId(UUID postId);
}