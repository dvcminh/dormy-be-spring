package com.minhvu.interaction.repository;

import com.minhvu.interaction.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IcommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
