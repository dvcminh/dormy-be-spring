package com.minhvu.monolithic.service;

import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.dto.response.GroupChatDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.GroupChat;

import java.util.List;
import java.util.UUID;

public interface GroupChatService {
    GroupChatDto createGroupChat(String name, String image, List<UUID> userIds);

    void addUserToGroupChat(UUID groupId, UUID userId);

    void removeUserFromGroupChat(UUID groupId, UUID userId);

    void updateGroupChat(UUID groupId, String name, String description, String avatar);

    void deleteGroupChat(UUID groupId);

    List<GroupChat> getGroupChats(AppUser userPrinciple);

    List<AppUserDto> getGroupChatUsers(UUID groupId);
}
