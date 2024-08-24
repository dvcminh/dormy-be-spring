package com.minhvu.friend.service;




import com.minhvu.friend.dto.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);

    boolean existsById(UUID userIdSender);
}
