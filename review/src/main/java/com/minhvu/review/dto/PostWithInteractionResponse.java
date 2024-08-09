package com.minhvu.review.dto;

import com.minhvu.review.dto.inter.InteractionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWithInteractionResponse {
    private PostResponse postResponse;
    private InteractionDto interactionDto;
}
