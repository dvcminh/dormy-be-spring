package com.minhvu.monolithic.repository;


import com.minhvu.monolithic.entity.Comment;
import com.minhvu.monolithic.entity.Like;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILIKE  extends JpaRepository<Like,Long> {

    Optional<Like> findByPostAndUser(Post postId, User id);

    Optional<Like> findByCommentAndUser(Comment comment, User user);

    List<Like> findByPost(Post id);

    List<Like> findByComment(Comment comment);
}
