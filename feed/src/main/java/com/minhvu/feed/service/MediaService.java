package com.minhvu.feed.service;

import com.minhvu.feed.dto.MediaDto;

import java.util.List;
import java.util.UUID;

public interface MediaService {
    List<MediaDto> getMediaByPostId(UUID id);
}
