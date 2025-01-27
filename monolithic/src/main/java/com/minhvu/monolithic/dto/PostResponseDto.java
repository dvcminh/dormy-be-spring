package com.minhvu.monolithic.dto;

import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.enums.PostType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class PostResponseDto {

    private UUID id;

    private String caption;

    private PostType postType;

    private String postContentUrl;

    private String thumbnailUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private AppUserDto user;

    private Set<AppUserDto> taggedUser;
}
