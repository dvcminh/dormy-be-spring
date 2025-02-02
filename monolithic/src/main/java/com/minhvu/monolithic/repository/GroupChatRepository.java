package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.GroupChat;
import com.minhvu.monolithic.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface GroupChatRepository extends JpaRepository<GroupChat, UUID> {

    List<GroupChat> findByUserGroupsIn(Collection<List<UserGroup>> userGroups);

}
