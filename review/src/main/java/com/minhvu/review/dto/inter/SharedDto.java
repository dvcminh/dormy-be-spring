package com.minhvu.review.dto.inter;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SharedDto {

    private UUID id;
    private UUID postId;
    private UUID userId;
    private LocalDateTime sharedAt;
}
