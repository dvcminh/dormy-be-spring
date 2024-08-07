package com.minhvu.authservice.dto;

import com.minhvu.authservice.entity.Role;

import java.util.UUID;

public record UserDto(UUID id,
                      String email,
                      String username,
                      String phoneNumber,
                      String address,
                      String avatar,
                      Role role) {
}
