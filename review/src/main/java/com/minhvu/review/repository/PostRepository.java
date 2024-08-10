package com.minhvu.review.repository;

import com.minhvu.review.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findPostEntitiesByUserId(Long id);

}
