package com.minhvu.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDto {
    private UUID id;
    private UUID userId;
    private UUID friendId;
    private LocalDateTime acceptedAt;
}
