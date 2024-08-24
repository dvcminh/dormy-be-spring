package com.minhvu.feed.client;

import com.minhvu.feed.dto.UserFriendDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "FRIEND-SERVICE/api/v1/friend")
public interface  FriendshipServiceClient {

    @GetMapping("/user/all/{id}")
    ResponseEntity<UserFriendDto> getFriends(@PathVariable("id") UUID userId);
}
