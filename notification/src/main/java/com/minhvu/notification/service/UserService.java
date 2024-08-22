package com.minhvu.notification.service;


import com.minhvu.notification.dto.AppUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {


    AppUserDto getUserById(UUID userId);

}
