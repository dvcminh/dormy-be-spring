package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CreateSharedRequest;
import com.minhvu.interaction.dto.SharedDto;
import com.minhvu.interaction.dto.UpdateSharedRequest;
import com.minhvu.interaction.dto.mapper.SharedMapper;
import com.minhvu.interaction.entity.Shared;
import com.minhvu.interaction.exception.NotFoundException;
import com.minhvu.interaction.kafka.SharedProducer;
import com.minhvu.interaction.repository.SharedRepository;
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

    private final SharedRepository sharedRepository;
    private final SharedMapper sharedMapper;
    private final SharedProducer sharedProducer;
    private final String SHARED_NOT_FOUND = "Shared not found with this id : ";

    @Override
    public SharedDto save(UUID userId, CreateSharedRequest createSharedRequest)
    {
        Shared sharedSaved = sharedRepository.saveAndFlush(
                Shared.builder().postId(createSharedRequest.getPostId())
                                .sharedText(createSharedRequest.getSharedText())
                                .userId(userId)
                                .build());
        SharedDto sharedDto = sharedMapper.toDto(sharedSaved);
        sharedProducer.send(sharedDto);
        return sharedDto;
    }

    @Override
    public SharedDto update(UUID id, UpdateSharedRequest sharedDto)
    {
        Shared shared = sharedRepository.findById(id).orElseThrow(() -> new NotFoundException(SHARED_NOT_FOUND + id));
        shared.setSharedText(sharedDto.getSharedText());
        Shared sharedSaved = sharedRepository.save(shared);
        return sharedMapper.toDto(sharedSaved);
    }

    @Override
    public SharedDto getById(UUID id)
    {
        Shared shared = sharedRepository.findById(id).orElseThrow(() -> new NotFoundException(SHARED_NOT_FOUND + id));
        return sharedMapper.toDto(shared);
    }





    @Override
    public List<SharedDto> getAll()
    {
        List<Shared> shareds = sharedRepository.findAll();
        return shareds
                .stream()
                .map(sharedMapper::toDto)
                .toList();
    }

    @Override
    public void delete(UUID id)
    {
        if(!sharedRepository.existsById(id))
        {
            throw new NotFoundException(SHARED_NOT_FOUND + id);
        }
        sharedRepository.deleteById(id);
    }
}
