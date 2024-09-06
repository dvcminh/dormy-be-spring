package com.minhvu.feed.repository;

import com.minhvu.feed.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {

}
