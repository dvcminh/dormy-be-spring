package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.entity.User;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPost extends JpaRepository<Post,Long> {
    List<Post> findAllByUser(User user);

    Page<Post> findByCaptionIgnoreCaseContaining(String query, Pageable pageable);

    Page<Post> findByUserIn(List<User> followedUser, Pageable pageable);

    List<Post> findByPostTypeAndUser_AccountType(PostType postType, AccountType accountType, Pageable pageable);

    List<Post> findByPostType(PostType postType,  Pageable pageable);
}
