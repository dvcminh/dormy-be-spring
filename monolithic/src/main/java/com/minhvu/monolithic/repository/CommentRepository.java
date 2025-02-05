package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.Comment;
import com.minhvu.monolithic.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {


    List<Comment> findAllByParentComment(Comment comment);

    List<Comment> findAllByParentCommentAndPost(Comment parentComment, Post post);

    long countByPost_Id(UUID id);
}
