package com.minhvu.feed.service;

import com.minhvu.feed.dto.SharedDto;
import com.minhvu.feed.dto.mapper.SharedMapper;
import com.minhvu.feed.model.Shared;
import com.minhvu.feed.repository.SharedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {
    private final SharedRepository isharedRepository;
    private final SharedMapper sharedMapper;
    @Override
    public List<SharedDto> getAllSharedByPostId(UUID postId)
    {
        List<Shared> shareds = isharedRepository.findByPostId(postId);
        return shareds
                .stream()
                .map(sharedMapper::toDto)
                .toList();
    }

    @Override
    public int getCountSharedsOfPost(UUID postId)
    {
        List<Shared> shareds = isharedRepository.findByPostId(postId);
        return shareds.size();
    }
}
