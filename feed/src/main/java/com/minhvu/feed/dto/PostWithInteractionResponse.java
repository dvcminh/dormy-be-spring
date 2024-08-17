package com.minhvu.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWithInteractionResponse {
    private PostResponse postResponse;
    private InteractionDto interactionDto;
}
