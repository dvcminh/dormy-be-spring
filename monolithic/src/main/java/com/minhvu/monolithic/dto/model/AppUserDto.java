package com.minhvu.monolithic.dto.model;

import com.minhvu.monolithic.entity.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
    private UUID id;
    private String username;
    private String displayName;
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
