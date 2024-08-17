package com.minhvu.interaction.service.Impl;

import com.minhvu.interaction.dto.SharedDto;
import com.minhvu.interaction.dto.mapper.SharedMapper;
import com.minhvu.interaction.entity.Shared;
import com.minhvu.interaction.exception.NotFoundException;
import com.minhvu.interaction.repository.IsharedRepository;
import com.minhvu.interaction.service.IsharedService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SharedService implements IsharedService {

    private final IsharedRepository isharedRepository;
    private final SharedMapper sharedMapper;
    private final String SHARED_NOT_FOUND = "Shared not found with this id : ";

    @Override
    public SharedDto save(Long postId, SharedDto sharedDto)
    {
        sharedDto.setSharedAt(LocalDateTime.now());
        sharedDto.setPostId(postId);
        Shared sharedSaved = isharedRepository.save(sharedMapper.toModel(sharedDto));
        return sharedMapper.toDto(sharedSaved);
    }

    @Override
    public SharedDto update(Long id, SharedDto sharedDto)
    {
        Shared shared = isharedRepository.findById(id).orElseThrow(() -> new NotFoundException(SHARED_NOT_FOUND + id));
        shared.setPostId(sharedDto.getPostId());
        shared.setUserId(sharedDto.getUserId());
        Shared sharedUpdated = isharedRepository.save(shared);
        return sharedMapper.toDto(sharedUpdated);
    }

    @Override
    public SharedDto getById(Long id)
    {
        Shared shared = isharedRepository.findById(id).orElseThrow(() -> new NotFoundException(SHARED_NOT_FOUND + id));
        return sharedMapper.toDto(shared);
    }

    @Override
    public List<SharedDto> getAllSharedByPostId(Long postId)
    {
        List<Shared> shareds = isharedRepository.findByPostId(postId);
        return shareds
                .stream()
                .map(sharedMapper::toDto)
                .toList();
    }

    @Override
    public int getCountSharedsOfPost(Long postId)
    {
        List<Shared> shareds = isharedRepository.findByPostId(postId);
        return shareds.size();
    }

    @Override
    public List<SharedDto> getAll()
    {
        List<Shared> shareds = isharedRepository.findAll();
        return shareds
                .stream()
                .map(sharedMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id)
    {
        if(!isharedRepository.existsById(id))
        {
            throw new NotFoundException(SHARED_NOT_FOUND + id);
        }
        isharedRepository.deleteById(id);
    }
}
