package com.minhvu.feed.service;

import com.minhvu.feed.dto.MediaDto;
import com.minhvu.feed.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService{
    private final MediaRepository mediaRepository;
    @Override
    public List<MediaDto> getMediaByPostId(UUID id) {
        return mediaRepository.findByPostId(id);
    }
}
