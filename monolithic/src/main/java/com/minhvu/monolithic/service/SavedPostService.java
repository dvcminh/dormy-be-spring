package com.minhvu.monolithic.service;

import com.minhvu.monolithic.entity.AppUser;

import java.util.List;
import java.util.UUID;

public interface SavedPostService {
    void savePost(AppUser user, UUID postId);

    void unsavePost(AppUser user, UUID postId);

    List<?> getSavedPosts(AppUser user);
}
