package com.minhvu.review.service;





import com.minhvu.review.dto.inter.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);

    boolean existsById(UUID userIdSender);
}
