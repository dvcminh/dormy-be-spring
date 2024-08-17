package com.minhvu.feed.client;

import com.minhvu.feed.dto.UserRelationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-relation-service")
public interface UserRelationClient {
    @GetMapping("/userrelation/{userId}")
    UserRelationDto getUserRelation(@PathVariable("userId") Long userId);

}
