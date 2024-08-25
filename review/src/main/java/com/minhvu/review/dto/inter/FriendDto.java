package com.minhvu.review.dto.inter;


import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDto {

    private UUID userId;

    private List<UUID> friendId;

}
