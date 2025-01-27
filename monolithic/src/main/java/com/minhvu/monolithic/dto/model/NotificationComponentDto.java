package com.minhvu.monolithic.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationComponentDto {
    private String entityType;
    private UUID entityId;
    private String componentName;
}
