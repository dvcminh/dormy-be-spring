package com.minhvu.monolithic.service;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.GroupChat;
import com.minhvu.monolithic.entity.UserGroup;
import com.minhvu.monolithic.repository.AppUserRepository;
import com.minhvu.monolithic.repository.GroupChatRepository;
import com.minhvu.monolithic.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupChatServiceImpl implements GroupChatService {
    private final GroupChatRepository groupChatRepository;
    private final AppUserRepository appUserRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public void createGroupChat(String name, String image, List<UUID> userIds) {
        GroupChat groupChat = GroupChat.builder()
                .name(name)
                .image(image)
                .build();
        groupChatRepository.save(groupChat);

        for (UUID userId : userIds) {
            AppUser user = appUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            UserGroup userGroup = UserGroup.builder()
                    .groupChat(groupChat)
                    .user(user)
                    .joinedAt(LocalDateTime.now())
                    .build();
            userGroupRepository.save(userGroup);
        }
    }

    @Override
    public void addUserToGroupChat(UUID groupId, UUID userId) {
        GroupChat groupChat = groupChatRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group chat not found"));
        AppUser user = appUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserGroup userGroup = UserGroup.builder()
                .groupChat(groupChat)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();
        userGroupRepository.save(userGroup);
    }

    @Override
    public void removeUserFromGroupChat(UUID groupId, UUID userId) {
        GroupChat groupChat = groupChatRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group chat not found"));
        AppUser user = appUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserGroup userGroup = userGroupRepository.findByGroupChatAndUser(groupChat, user).orElseThrow(() -> new RuntimeException("User not found in group chat"));
        userGroupRepository.delete(userGroup);
    }

    @Override
    public void updateGroupChat(UUID groupId, String name, String description, String avatar) {
        GroupChat groupChat = groupChatRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group chat not found"));
        groupChat.setName(name);
        groupChat.setImage(avatar);
        groupChatRepository.save(groupChat);
    }

    @Override
    public void deleteGroupChat(UUID groupId) {
        GroupChat groupChat = groupChatRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group chat not found"));
        groupChatRepository.delete(groupChat);
    }
}
