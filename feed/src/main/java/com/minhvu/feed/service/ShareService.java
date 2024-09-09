package com.minhvu.feed.service;

import com.minhvu.feed.dto.SharedDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface ShareService {
    List<SharedDto> getAllSharedByPostId(UUID postId);

    int getCountSharedsOfPost(UUID postId);
}
