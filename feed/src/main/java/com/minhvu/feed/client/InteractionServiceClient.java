package com.minhvu.feed.client;

import com.minhvu.feed.dto.CompletReaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "INTERACTIONS-SERVICE/api/v1/reactions")
public interface InteractionServiceClient {

    @GetMapping("/post/{postId}")
    List<CompletReaction> getReactionsByPostId(@PathVariable("postId") Long postId);

}
