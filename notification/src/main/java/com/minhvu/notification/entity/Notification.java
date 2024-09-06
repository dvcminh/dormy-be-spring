package com.minhvu.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET is_delete = true WHERE id=?")
@Where(clause = "is_delete = false")
@Builder
public class Notification extends BaseEntity {

    private UUID relatedId;
    private String message;
    private boolean seen;
    private UUID userReceiver;
    private boolean isDelete = Boolean.FALSE;
}
