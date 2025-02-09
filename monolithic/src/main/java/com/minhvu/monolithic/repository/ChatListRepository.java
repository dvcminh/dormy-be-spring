package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.ChatList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatListRepository extends JpaRepository<ChatList, UUID> {
    List<ChatList> findByUserOrderByLastMessageTimeDesc(AppUser user);

    Optional<ChatList> findByUserAndContact(AppUser user, AppUser contact);
}
