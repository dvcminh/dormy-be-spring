package com.minhvu.feed.service;

import com.minhvu.feed.dto.ReactionDto;

import java.util.List;
import java.util.UUID;

public interface ReactionService {
    List<ReactionDto> getAllReactionsByPostId(UUID postId);

    int getCountReactionsOfPost(UUID postId);

    int getLikeCountOfPost(UUID postId);

    int getLoveCountOfPost(UUID postId);

    int getWowCountOfPost(UUID postId);

    int getAngryCountOfPost(UUID postId);

    int getSadCountOfPost(UUID postId);

    int getHahahCountOfPost(UUID postId);
}
