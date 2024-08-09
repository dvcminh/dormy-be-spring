package com.minhvu.feed.client;

import com.minhvu.feed.dto.UserRelationDto;
import com.minhvu.feed.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserRelationClientFallback implements UserRelationClient{
    @Override
    public UserRelationDto getUserRelation(Long userId) {
        throw new NotFoundException("The relations of that user not found"+userId);
    }
}
