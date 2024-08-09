package com.minhvu.notification.repository;

import com.minhvu.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserReceiverAndSeenIsFalse(Long userReceiver);
}
