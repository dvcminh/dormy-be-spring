package com.minhvu.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationUser {
    private NotificationComponent component;
    private UUID userId;
    private Boolean isRead;
}
