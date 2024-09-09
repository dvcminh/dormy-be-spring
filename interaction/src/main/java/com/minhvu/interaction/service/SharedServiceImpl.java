package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CreateSharedRequest;
import com.minhvu.interaction.dto.SharedDto;
import com.minhvu.interaction.dto.UpdateSharedRequest;
import com.minhvu.interaction.dto.mapper.SharedMapper;
import com.minhvu.interaction.entity.Shared;
import com.minhvu.interaction.exception.NotFoundException;
import com.minhvu.interaction.repository.IsharedRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SharedServiceImpl implements SharedService {

    private final IsharedRepository isharedRepository;
    private final SharedMapper sharedMapper;
    private final String SHARED_NOT_FOUND = "Shared not found with this id : ";

    @Override
    public SharedDto save(UUID userId, CreateSharedRequest sharedDto)
    {
        Shared shared = Shared.builder()
                .postId(sharedDto.getPostId())
                .sharedText(sharedDto.getSharedText())
                .userId(userId)
                .build();
        Shared sharedSaved = isharedRepository.save(shared);
        return sharedMapper.toDto(sharedSaved);
    }

    @Override
    public SharedDto update(UUID id, UpdateSharedRequest sharedDto)
    {
        Shared shared = isharedRepository.findById(id).orElseThrow(() -> new NotFoundException(SHARED_NOT_FOUND + id));
        shared.setSharedText(sharedDto.getSharedText());
        shared.setUpdatedAt(new Date());
        Shared sharedSaved = isharedRepository.save(shared);
        return sharedMapper.toDto(sharedSaved);
    }

    @Override
    public SharedDto getById(UUID id)
    {
        Shared shared = isharedRepository.findById(id).orElseThrow(() -> new NotFoundException(SHARED_NOT_FOUND + id));
        return sharedMapper.toDto(shared);
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
    public void delete(UUID id)
    {
        if(!isharedRepository.existsById(id))
        {
            throw new NotFoundException(SHARED_NOT_FOUND + id);
        }
        isharedRepository.deleteById(id);
    }
}
