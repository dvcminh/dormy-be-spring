package com.minhvu.notification.service;

import com.minhvu.notification.dto.NotificationDto;
import com.minhvu.review.dto.PostProducerDto;

import java.util.List;
import java.util.UUID;

public interface INotificationService {

    void sendPostCreationNotification(PostProducerDto post);
    List<NotificationDto> getUnseenNotifications(UUID user);
    void markNotificationAsSeen(UUID notificationId);
}
