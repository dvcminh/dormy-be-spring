package com.minhvu.feed.client;

import com.minhvu.feed.dto.CompletReaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "reaction-service", fallback = InteractionFallback.class)
public interface InteractionServiceClient {

    @GetMapping("/reaction-service/reaction/{postId}")
    List<CompletReaction> getReactionsByPostId(Long postId);

}
