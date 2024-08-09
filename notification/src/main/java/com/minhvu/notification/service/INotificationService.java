package com.minhvu.notification.service;

import com.minhvu.notification.dto.NotificationDto;
import com.minhvu.review.dto.PostProducerDto;

import java.util.List;

public interface INotificationService {

    void sendPostCreationNotification(PostProducerDto post);
    List<NotificationDto> getUnseenNotifications(Long user);
    void markNotificationAsSeen(Long notificationId);
}
