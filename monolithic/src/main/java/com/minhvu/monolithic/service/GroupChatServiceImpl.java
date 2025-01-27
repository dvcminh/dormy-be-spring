package com.minhvu.monolithic.service;

import com.minhvu.monolithic.repository.AppUserRepository;
import com.minhvu.monolithic.repository.GroupChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupChatServiceImpl {
    private final GroupChatRepository groupChatRepository;
    private final AppUserRepository appUserRepository;

//    public void createGroupChat(CreateGroupChatRequest createGroupChatRequest) {
//        List<AppUser> members = createGroupChatRequest.getUserIds().stream()
//                .map(memberId -> appUserRepository.findById(memberId).get())
//                .toList();
//        GroupChat groupChat = GroupChat.builder()
//                .name(createGroupChatRequest.getName())
//                .image(createGroupChatRequest.getImage())
//                .userGroups(members)
//                .build();
//
//    }
}
