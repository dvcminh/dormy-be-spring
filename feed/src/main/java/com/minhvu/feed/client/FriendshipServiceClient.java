package com.minhvu.feed.client;

import com.minhvu.feed.dto.FriendShip;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "FRIEND-SERVICE/api/friend")
public interface  FriendshipServiceClient {

    @GetMapping("/profiles/all/{id}")
    List<FriendShip> getFriends(@PathVariable("id") Long userId);
}
