package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.model.NotificationDto;
import com.minhvu.monolithic.dto.model.NotificationUserDto;
import com.minhvu.monolithic.dto.model.NotificationUserResponseDto;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.dto.response.page.PageLink;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Notification;

import java.util.UUID;

public interface NotificationService {
    PageData<NotificationUserResponseDto> getNotificationUsers(PageLink pageLink, Boolean isRead, AppUser appUser);

    void receiveNotificationUser(NotificationUserDto notificationUserDto);
    void readNotificationUser(UUID id, AppUser appUser);

    void sendNotificationToUser(UUID userId, Notification notification);

    NotificationDto generateNotification(UUID toUserIds, String message, String description, String entityType, UUID entityId, UUID createdBy);

    void saveNotification(NotificationDto notification);
}
