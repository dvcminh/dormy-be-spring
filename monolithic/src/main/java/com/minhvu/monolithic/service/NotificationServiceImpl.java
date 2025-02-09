package com.minhvu.monolithic.service;

import com.minhvu.monolithic.dto.mapper.AppUserMapper;
import com.minhvu.monolithic.dto.model.NotificationComponentDto;
import com.minhvu.monolithic.dto.model.NotificationDto;
import com.minhvu.monolithic.dto.model.NotificationUserDto;
import com.minhvu.monolithic.dto.model.NotificationUserResponseDto;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.dto.response.page.PageLink;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Notification;
import com.minhvu.monolithic.entity.NotificationComponent;
import com.minhvu.monolithic.entity.NotificationUser;
import com.minhvu.monolithic.repository.AppUserRepository;
import com.minhvu.monolithic.repository.NotificationComponentRepository;
import com.minhvu.monolithic.repository.NotificationRepository;
import com.minhvu.monolithic.repository.NotificationUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationComponentRepository notificationComponentRepository;
    private final AppUserMapper appUserMapper;
    private final AppUserRepository appUserRepository;

    @Override
    public PageData<NotificationUserResponseDto> getNotificationUsers(PageLink pageLink, Boolean isRead, AppUser appUser) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        Page<NotificationUser> notificationUser = notificationUserRepository.findByUser_IdAndIsRead(
                appUser.getId(),
                isRead,
                pageable);
        Page<NotificationUserResponseDto> notificationUserPage = notificationUser.map(
                n -> NotificationUserResponseDto.builder()
                        .componentName(n.getNotification().getComponent().getComponentName())
                        .entityType(n.getNotification().getComponent().getEntityType())
                        .entityId(n.getNotification().getComponent().getEntityId())
                        .sentTo(appUserMapper.toDto(n.getUser()))
                        .isRead(n.getIsRead())
                        .createdAt(n.getNotification().getCreatedAt())
                        .message(n.getNotification().getMessage())
                        .description(n.getNotification().getDescription())
                        .createdBy(appUserMapper.toDto(n.getNotification().getCreatedBy()))
                        .build());
        return new PageData<>(notificationUserPage);
    }

    @Override
    public void receiveNotificationUser(NotificationUserDto notificationUserDto) {
        log.info("** Saving notification to repository payload: '{}'", notificationUserDto.toString());
        NotificationComponent notificationComponent = notificationComponentRepository
                .findByEntityId(notificationUserDto.getComponent().getEntityId());
        Notification notification = notificationRepository.findByComponent(notificationComponent);
        NotificationUser notificationUser = notificationUserRepository.findByNotificationAndUserId(notification, notificationUserDto.getUserId());
        notificationUser.setIsRead(notificationUserDto.getIsRead());
        notificationUserRepository.save(notificationUser);
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(notificationUser.getUser()), "/notification", notification);
    }

    @Override
    public void readNotificationUser(UUID id, AppUser appUser) {
        NotificationUser notificationUser = notificationUserRepository.findByIdAndUserId(id, appUser.getId());
        notificationUser.setIsRead(true);
        notificationUserRepository.save(notificationUser);
    }

    @Override
    public NotificationDto generateNotification(UUID toUserIds, String message, String description, String entityType, UUID entityId, UUID createdBy) {
        Collection<UUID> userIds = new ArrayList<>();
        userIds.add(toUserIds);
        AppUser createdByUser = appUserRepository.findById(createdBy).orElseThrow(() -> new RuntimeException("User not found"));
        return NotificationDto.builder()
                .message(message)
                .description(description)
                .component(NotificationComponentDto.builder()
                        .entityType(entityType)
                        .entityId(entityId)
                        .build())
                .toUserIds(userIds)
                .createdBy(appUserMapper.toDto(createdByUser))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public void saveNotification(NotificationDto notificationDto) {
        NotificationComponent notificationComponent = new NotificationComponent();
        BeanUtils.copyProperties(notificationDto.getComponent(), notificationComponent);
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationDto, notification, "component");
        notification.setComponent(notificationComponent);
        AppUser createdBy = appUserRepository.findById(notificationDto.getCreatedBy().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        notification.setCreatedBy(createdBy);
        notificationRepository.save(notification);
        Collection<UUID> userIds = notificationDto.getToUserIds();

        log.info(userIds.toString());
        for (UUID userId : userIds) {
            AppUser user = appUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            NotificationUser notificationUser = NotificationUser.builder()
                    .notification(notification)
                    .user(user)
                    .isRead(false)
                    .build();
            notificationUserRepository.save(notificationUser);
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), "/private-notification", notification);
        }

    }
}
