package com.minhvu.friend.service;

import com.minhvu.friend.dto.AppUserDto;
import com.minhvu.friend.dto.mapper.AppUserMapper;
import com.minhvu.friend.model.entities.AppUser;
import com.minhvu.friend.repository.AppUserRepository;
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

    @Override
    public boolean existsById(UUID userIdSender) {
        return userRepository.existsById(userIdSender);
    }

}
