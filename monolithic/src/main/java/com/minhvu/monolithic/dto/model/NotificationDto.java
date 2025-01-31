package com.minhvu.monolithic.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private NotificationComponentDto component;
    private String message;
    private String description;
    private LocalDateTime createdAt;
    private Collection<UUID> toUserIds;
    private AppUserDto createdBy;
}
