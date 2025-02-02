package com.minhvu.monolithic.dto.mapper;


import com.minhvu.monolithic.dto.response.GroupChatDto;
import com.minhvu.monolithic.entity.GroupChat;
import org.mapstruct.Mapper;


@Mapper()
public interface GroupChatMapper {

    GroupChatDto toDto(GroupChat groupChat);

}
