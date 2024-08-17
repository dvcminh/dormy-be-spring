package com.minhvu.review.client;

import com.minhvu.review.dto.inter.FriendDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FRIEND-SERVICE/api/v1/friend")
public interface FriendClient {
    @GetMapping("/user/all/{id}")
    ResponseEntity<FriendDto> getFriendsOfUser(@PathVariable("id") Long userId);
}
