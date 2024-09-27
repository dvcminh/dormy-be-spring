package com.minhvu.notification.service;

import com.minhvu.notification.dto.model.AppUserDto;
import com.minhvu.notification.dto.model.NotificationUserResponseDto;
import com.minhvu.notification.dto.page.PageData;
import com.minhvu.notification.dto.page.PageLink;
import com.minhvu.notification.model.NotificationUser;
import com.minhvu.notification.repository.NotificationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationUserRepository notificationUserRepository;

    @Override
    public PageData<NotificationUserResponseDto> getNotificationUsers(PageLink pageLink, Boolean isRead, AppUserDto appUserDto) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        Page<NotificationUser> notificationUser = notificationUserRepository.findByUserIdAndIsRead(
                appUserDto.getId(),
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
}
