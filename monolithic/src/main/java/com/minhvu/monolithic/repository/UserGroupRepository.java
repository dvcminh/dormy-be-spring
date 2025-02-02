package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.GroupChat;
import com.minhvu.monolithic.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

    Optional<UserGroup> findByGroupChatAndUser(GroupChat groupChat, AppUser user);

    List<UserGroup> findByUser(AppUser user);

    List<UserGroup> findByGroupChat(GroupChat groupChat);
}
