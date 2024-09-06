package com.minhvu.interaction.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateSharedRequest {
    private String sharedText;
    private UUID postId;
}
