package com.minhvu.feed.service;



import com.minhvu.feed.dto.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);
}
