package com.minhvu.review.client;

import com.minhvu.review.dto.InteractionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "INTERACTIONS-SERVICE/api/v1/interactions")
public interface InteractionClient {

    @GetMapping("/post/{postId}")
     ResponseEntity<InteractionDto> getInteractionsOfPost(@PathVariable UUID postId);
}
