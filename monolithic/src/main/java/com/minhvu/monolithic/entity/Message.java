package com.minhvu.monolithic.entity;

import com.minhvu.monolithic.entity.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "messages")
public class Message extends BaseEntity {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;
}
