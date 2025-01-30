package com.minhvu.monolithic.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationUserResponseDto {
    private String componentName;
    private String entityType;
    private UUID entityId;
    private AppUserDto userId;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private String message;
    private String description;
}
