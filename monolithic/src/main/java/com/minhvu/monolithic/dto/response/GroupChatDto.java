package com.minhvu.monolithic.dto.response;

import com.minhvu.monolithic.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GroupChatDto {
    public UUID id;
    public String name;
    public String image;
    public AppUser host;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}

