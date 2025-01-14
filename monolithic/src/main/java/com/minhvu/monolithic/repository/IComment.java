package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.Comment;
import com.minhvu.monolithic.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IComment extends JpaRepository<Comment,Long>{


    List<Comment> findAllByParentComment(Comment comment);

    List<Comment> findAllByParentCommentAndPost(Comment parentComment, Post post);
}
