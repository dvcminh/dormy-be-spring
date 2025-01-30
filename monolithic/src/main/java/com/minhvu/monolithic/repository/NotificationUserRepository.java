package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.Notification;
import com.minhvu.monolithic.entity.NotificationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, UUID> {
    NotificationUser findByNotificationAndUserId(Notification notification, UUID userId);

    Page<NotificationUser> findByUser_IdAndIsRead(UUID id, Boolean isRead, Pageable pageable);

    NotificationUser findByIdAndUserId(UUID id, UUID id1);

}
