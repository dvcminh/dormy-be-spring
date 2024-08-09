package com.minhvu.notification.consumer;

import com.minhvu.notification.service.INotificationService;
import com.minhvu.review.dto.PostProducerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final INotificationService iNotificationService;

    @KafkaListener(topics = "post-topic", groupId = "myGroup")
    public void sendNotifForPostToAllFriend(PostProducerDto postNotif)
    {
        iNotificationService.sendPostCreationNotification(postNotif);
        //log.info(String.format("Consuming the message from post-topic topic: %s", postNotif));
    }
}
