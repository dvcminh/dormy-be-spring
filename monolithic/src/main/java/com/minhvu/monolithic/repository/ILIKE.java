package com.minhvu.monolithic.repository;


import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Comment;
import com.minhvu.monolithic.entity.Like;
import com.minhvu.monolithic.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ILIKE  extends JpaRepository<Like, UUID> {

    Optional<Like> findByPostAndUser(Post postId, AppUser id);

    Optional<Like> findByCommentAndUser(Comment comment, AppUser user);

    List<Like> findByPost(Post id);

    List<Like> findByComment(Comment comment);
}
