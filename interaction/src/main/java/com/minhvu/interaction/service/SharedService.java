package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.SharedDto;

import java.util.List;
import java.util.UUID;

public interface SharedService {

    SharedDto save(UUID postId, SharedDto sharedDto);
    SharedDto update(UUID id, SharedDto sharedDto);
    SharedDto getById(UUID id);
    List<SharedDto> getAllSharedByPostId(UUID postId);
    int getCountSharedsOfPost(UUID postId);
    List<SharedDto> getAll();
    void delete(UUID id);
}
