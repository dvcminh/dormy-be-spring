package com.minhvu.monolithic.service;

import java.util.List;
import java.util.UUID;

public interface GroupChatService {
    void createGroupChat(String name, String image, List<UUID> userIds);

    void addUserToGroupChat(UUID groupId, UUID userId);

    void removeUserFromGroupChat(UUID groupId, UUID userId);

    void updateGroupChat(UUID groupId, String name, String description, String avatar);

    void deleteGroupChat(UUID groupId);
}
