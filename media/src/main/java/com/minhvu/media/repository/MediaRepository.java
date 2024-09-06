package com.minhvu.media.repository;

import com.minhvu.media.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {
    Optional<Media> findByMediaUuid(String mediaUuid);
    List<Media> findByPostId(UUID postId);

}
