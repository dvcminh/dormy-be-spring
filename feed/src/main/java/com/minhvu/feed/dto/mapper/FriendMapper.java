package com.minhvu.feed.dto.mapper;

import com.minhvu.feed.dto.FriendDto;
import com.minhvu.feed.model.Friend;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface FriendMapper {
    FriendDto toDto(Friend friend);
    Friend toModel(FriendDto friendDto);
}
