package com.minhvu.friend.dto;

import com.minhvu.friend.model.enums.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestDto implements Serializable {
    UUID id;
    UUID userIdSender;
    UUID friendId;
    Status status;
    Date createdAt;
    Date updatedAt;
}