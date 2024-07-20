package com.minhvu.review.service;

import com.minhvu.review.dto.model.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);
}
