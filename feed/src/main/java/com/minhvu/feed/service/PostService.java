package com.minhvu.feed.service;

import com.minhvu.feed.dto.PostWithInteractionResponse;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<PostWithInteractionResponse> getPostsByUserId(UUID friendId);
}
