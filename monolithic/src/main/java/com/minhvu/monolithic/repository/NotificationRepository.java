package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.Notification;
import com.minhvu.monolithic.entity.NotificationComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Notification findByComponent(NotificationComponent notificationComponent);
}
