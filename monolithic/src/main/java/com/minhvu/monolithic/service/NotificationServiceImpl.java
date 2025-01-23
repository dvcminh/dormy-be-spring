package com.minhvu.monolithic.service;

import com.minhvu.monolithic.dto.model.NotificationUserResponseDto;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.dto.response.page.PageLink;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Notification;
import com.minhvu.monolithic.entity.NotificationUser;
import com.minhvu.monolithic.repository.NotificationRepository;
import com.minhvu.monolithic.repository.NotificationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public PageData<NotificationUserResponseDto> getNotificationUsers(PageLink pageLink, Boolean isRead, AppUser appUser) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        Page<NotificationUser> notificationUser = notificationUserRepository.findByUserIdAndIsRead(
                appUser.getId(),
                isRead,
                pageable);
        Page<NotificationUserResponseDto> notificationUserPage = notificationUser.map(
                n -> NotificationUserResponseDto.builder()
                        .componentName(n.getNotification().getComponent().getComponentName())
                        .entityType(n.getNotification().getComponent().getEntityType())
                        .entityId(n.getNotification().getComponent().getEntityId())
                        .userId(n.getUserId())
                        .isRead(n.getIsRead())
                        .createdAt(n.getNotification().getCreatedAt())
                        .build());
        return new PageData<>(notificationUserPage);
    }

    @Override
    public void readNotificationUser(UUID id, AppUser appUser) {
        NotificationUser notificationUser = notificationUserRepository.findByIdAndUserId(id, appUser.getId());
        notificationUser.setIsRead(true);
        notificationUserRepository.save(notificationUser);
    }

    @Override
    public void sendNotificationToUser(UUID userId, Notification notification) {
        simpMessagingTemplate.convertAndSendToUser(userId.toString(), "/notification", notification);

        notificationRepository.save(notification);
        notificationUserRepository.save(NotificationUser.builder()
                .notification(notification)
                .userId(userId)
                .isRead(false)
                .build());
    }
}
