package com.minhvu.authservice.dto;

import com.minhvu.authservice.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
    private Long id;
    private String email;
    private String name;
    private String phone_number;
    private String address;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;
}
