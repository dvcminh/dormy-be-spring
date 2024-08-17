package com.minhvu.feed.client;

import com.minhvu.feed.dto.PostDto;
import com.minhvu.feed.dto.PostWithInteractionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "REVIEW-SERVICE", url = "http://localhost:8074")
public interface PostServiceClient {

    @GetMapping("/api/v1/post/all/{userId}")
    ResponseEntity<List<PostWithInteractionResponse>> getPostByUser(@PathVariable("userId") Long userId);


}
