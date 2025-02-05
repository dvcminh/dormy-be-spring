package com.minhvu.monolithic.service;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.repository.CommentRepository;
import com.minhvu.monolithic.repository.LikeRepository;
import com.minhvu.monolithic.repository.PostRepository;
import com.minhvu.monolithic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    // 1. User Growth Rate
    public double calculateUserGrowthRate() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime lastMonth = today.minusMonths(1);

        long usersThisMonth = userRepository.countUsersRegisteredAfter(lastMonth);
        long usersLastMonth = userRepository.countUsersRegisteredBetween(lastMonth.minusMonths(1), lastMonth);

        if (usersLastMonth == 0) return 0;
        return ((double) (usersThisMonth - usersLastMonth) / usersLastMonth) * 100;
    }

    // 2. Active Users Probability
    public double calculateActiveUserProbability(UUID userId) {
        AppUser user = userRepository.findById(userId).orElse(null);
        if (user == null) return 0;

        long totalPosts = postRepository.countByUserId(userId);
        long recentPosts = postRepository.countByUserIdAndCreatedAtAfter(userId, LocalDateTime.now().minusWeeks(1));

        if (totalPosts == 0) return 0;
        return (double) recentPosts / totalPosts;
    }

    // 3. Post Engagement Rate
    public double calculatePostEngagementRate(UUID postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) return 0;

        long likes = likeRepository.countByPost_Id(postId);
        long comments = commentRepository.countByPost_Id(postId);
        long totalUsers = userRepository.count();

        if (totalUsers == 0) return 0;
        return ((double) (likes + comments) / totalUsers) * 100;
    }
}
