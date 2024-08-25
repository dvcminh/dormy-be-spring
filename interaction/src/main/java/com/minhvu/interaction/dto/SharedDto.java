package com.minhvu.interaction.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    private Date sharedAt;
}
