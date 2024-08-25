package com.minhvu.interaction.service;





import com.minhvu.interaction.dto.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);

    boolean existsById(UUID userIdSender);
}
