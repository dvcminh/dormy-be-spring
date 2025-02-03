package com.minhvu.monolithic.service;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.entity.SavedPost;
import com.minhvu.monolithic.exception.NotFoundException;
import com.minhvu.monolithic.repository.PostRepository;
import com.minhvu.monolithic.repository.SavedPostRepository;
import com.minhvu.monolithic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavedPostServiceImpl implements SavedPostService {

    private final SavedPostRepository savedPostRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public void savePost(AppUser user, UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        if (savedPostRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("Post already saved");
        }

        SavedPost savedPost = SavedPost.builder()
                .user(user)
                .post(post)
                .build();

        savedPostRepository.save(savedPost);
    }

    @Override
    public void unsavePost(AppUser user, UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        SavedPost savedPost = savedPostRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new NotFoundException("Saved post not found"));

        savedPostRepository.delete(savedPost);
    }

    @Override
    public List<?> getSavedPosts(AppUser user) {
        return savedPostRepository.findAllByUser(user);
    }
}
