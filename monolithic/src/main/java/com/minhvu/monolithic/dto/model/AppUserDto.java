package com.minhvu.monolithic.dto.model;

import com.minhvu.monolithic.entity.enums.RoleType;
import com.minhvu.monolithic.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
    private UUID id;
    private String username;
    private String bio;
    private Gender gender;
    private String displayName;
    @Enumerated(EnumType.STRING)
    private RoleType role;
    private String profilePicture;
}
