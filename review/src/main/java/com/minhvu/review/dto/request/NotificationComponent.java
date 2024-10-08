package com.minhvu.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationComponent {
    private String componentName;
    private String entityType;
    private UUID entityId;
}
