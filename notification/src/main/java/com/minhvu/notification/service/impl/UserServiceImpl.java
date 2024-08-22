package com.minhvu.notification.service.impl;

import com.minhvu.notification.dto.AppUserDto;
import com.minhvu.notification.dto.mapper.AppUserMapper;
import com.minhvu.notification.repository.AppUserRepository;
import com.minhvu.notification.service.UserService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AppUserMapper userMapper;
    @Autowired
    private AppUserRepository appUserRepository;


    @Override
    public AppUserDto getUserById(UUID userId) {
        return userMapper.toDto(appUserRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found")));
    }

}
