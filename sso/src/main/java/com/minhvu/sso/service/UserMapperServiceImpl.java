package com.minhvu.sso.service;

import com.minhvu.sso.dto.mapper.AppUserMapper;
import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserMapperServiceImpl implements UserMapperService {

    private final AppUserRepository userRepository;

    private final AppUserMapper userMapper;

    @Override
    public AppUserDto findById(UUID id) {
        if (id == null) return null;
        return userMapper.toDto(userRepository.findById(id).orElse(null));
    }
}
