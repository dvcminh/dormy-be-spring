package com.minhvu.notification.service.impl;

import com.minhvu.notification.client.FriendClient;
import com.minhvu.notification.dto.NotificationDto;
import com.minhvu.notification.dto.UserDTO;
import com.minhvu.notification.entity.Notification;
import com.minhvu.notification.exception.NotFoundException;
import com.minhvu.notification.repository.INotificationRepository;
import com.minhvu.notification.service.INotificationService;
import com.minhvu.review.dto.PostProducerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final INotificationRepository iNotificationRepository;
    private final ModelMapper modelMapper;
    private final FriendClient friendClient;
    private static final String NOTIFICATION_NOT_FOUND = "Notification not found with this id : ";

    @Override
    public void sendPostCreationNotification(PostProducerDto postNotif)
    {
        List<UserDTO> users = friendClient.getAllFriendsProfile(String.valueOf(postNotif.getSenderId())).getBody();

        assert users != null;
        for (UserDTO receivedFriend : users)
        {

            notifyFriends(
                    Notification
                            .builder()
                            .relatedId(postNotif.getRelatedId())
                            .message(postNotif.getBody())
                            .userReceiver(receivedFriend.getId())
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        }
    }

    public void notifyFriends(Notification notification)
    {
       // log.info("friend notifiy : {}", notification);
        iNotificationRepository.save(notification);
    }

    @Override
    public List<NotificationDto> getUnseenNotifications(Long userReceiver)
    {
       List<Notification> notifications = iNotificationRepository.findByUserReceiverAndSeenIsFalse(userReceiver);
        return notifications
                .stream()
                .map((notification) -> modelMapper.map(notification, NotificationDto.class))
                .toList();
    }

    @Override
    public void markNotificationAsSeen(Long notificationId)
    {
        Notification notification = iNotificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException(NOTIFICATION_NOT_FOUND + notificationId));
        notification.setSeen(true);
        iNotificationRepository.save(notification);
    }
}
