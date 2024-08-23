package com.minhvu.feed.service;

import com.minhvu.feed.dto.AppUserDto;
import com.minhvu.feed.dto.mapper.AppUserMapper;
import com.minhvu.feed.model.AppUser;
import com.minhvu.feed.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AppUserRepository userRepository;

    @Autowired
    AppUserMapper userMapper;

    @Override
    public AppUserDto findByUserId(UUID userId) {
        AppUser user = userRepository.findById(userId).orElse(null);
        return user != null ? userMapper.toDto(user) : null;
    }
}
