package com.minhvu.friend.dto;

import com.minhvu.friend.model.enums.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestDto implements Serializable {
    Long id;
    Long userIdSender;
    Long friendId;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}