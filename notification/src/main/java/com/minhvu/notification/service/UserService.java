package com.minhvu.notification.service;


import com.minhvu.notification.dto.model.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);
}
