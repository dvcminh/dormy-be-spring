package com.minhvu.friend.dto;


import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFriendDto {

    private UUID userId;
    private List<UUID> friendId;

}
