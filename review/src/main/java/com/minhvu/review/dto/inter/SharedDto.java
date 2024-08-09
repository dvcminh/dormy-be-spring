package com.minhvu.review.dto.inter;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SharedDto {

    private Long id;
    private Long postId;
    private Long userId;
    private LocalDateTime sharedAt;
}
