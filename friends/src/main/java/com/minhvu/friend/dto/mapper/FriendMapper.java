package com.minhvu.friend.dto.mapper;

import com.minhvu.friend.dto.FriendDto;
import com.minhvu.friend.model.entities.Friend;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface FriendMapper {
    FriendDto toDto(Friend friend);
    Friend toModel(FriendDto friendDto);
}
