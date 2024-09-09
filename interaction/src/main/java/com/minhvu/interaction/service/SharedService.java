package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CreateSharedRequest;
import com.minhvu.interaction.dto.SharedDto;
import com.minhvu.interaction.dto.UpdateSharedRequest;

import java.util.List;
import java.util.UUID;

public interface SharedService {

    SharedDto save(UUID userId, CreateSharedRequest sharedDto);
    SharedDto update(UUID id, UpdateSharedRequest sharedDto);
    SharedDto getById(UUID id);
    List<SharedDto> getAll();
    void delete(UUID id);
}
