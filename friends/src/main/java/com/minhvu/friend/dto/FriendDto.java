package com.minhvu.friend.dto;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDto {

    private Long userId;

    private List<Long> friendId;

}
