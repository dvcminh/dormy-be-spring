package com.minhvu.friend.model.entities;

import com.minhvu.friend.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "friend_request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest extends BaseEntity {

    private UUID userIdSender;
    private UUID friendId;
    @Enumerated(EnumType.STRING)
    private Status status;
}
