package com.minhvu.sso.service;

import com.minhvu.sso.dto.model.AppUserDto;

import java.util.UUID;

public interface UserMapperService {
    AppUserDto findById(UUID id);

}
