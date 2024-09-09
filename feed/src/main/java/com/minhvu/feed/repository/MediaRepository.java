package com.minhvu.feed.repository;

import com.minhvu.feed.dto.MediaDto;
import com.minhvu.feed.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {

    List<MediaDto> findByPostId(UUID postId);
}
