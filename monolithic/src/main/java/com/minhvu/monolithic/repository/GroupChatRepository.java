package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupChatRepository extends JpaRepository<GroupChat, UUID> {

}
