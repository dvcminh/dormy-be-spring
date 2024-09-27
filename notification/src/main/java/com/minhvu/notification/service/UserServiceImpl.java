package com.minhvu.notification.service;

import com.minhvu.notification.dto.mapper.AppUserMapper;
import com.minhvu.notification.dto.model.AppUserDto;
import com.minhvu.notification.model.AppUser;
import com.minhvu.notification.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    AppUserRepository userRepository;

    @Autowired
    AppUserMapper userMapper;

    @Override
    public AppUserDto findByUserId(UUID userId) {
        AppUser user = userRepository.findById(userId).orElse(null);
        return user == null ? null : userMapper.toDto(user);
    }
}
