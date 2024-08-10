package com.minhvu.review.client;

import com.minhvu.review.dto.inter.InteractionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "INTERACTIONS/api/v1/interactions")
public interface InteractionClient {

    @GetMapping("/post/{postId}")
     ResponseEntity<InteractionDto> getInteractionsOfPost(@PathVariable Long postId);
}
