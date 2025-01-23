package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.NotificationComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationComponentRepository extends JpaRepository<NotificationComponent, UUID> {
    NotificationComponent findByEntityId(UUID entityId);
}
