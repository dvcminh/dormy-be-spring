package com.minhvu.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostProducerDto {

    private UUID relatedId;
    private String body;
    private UUID senderId;
    private UUID receivedId;
}
