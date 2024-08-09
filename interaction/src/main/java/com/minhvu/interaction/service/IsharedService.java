package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.SharedDto;

import java.util.List;

public interface IsharedService {

    SharedDto save(Long postId, SharedDto sharedDto);
    SharedDto update(Long id, SharedDto sharedDto);
    SharedDto getById(Long id);
    List<SharedDto> getAllSharedByPostId(Long postId);
    int getCountSharedsOfPost(Long postId);
    List<SharedDto> getAll();
    void delete(Long id);
}
