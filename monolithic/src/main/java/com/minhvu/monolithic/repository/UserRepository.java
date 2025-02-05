package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    List<AppUser> findByUsernameIgnoreCaseContainingOrDisplayNameIgnoreCaseContaining(String query, String query1);

    @Query("SELECT COUNT(u) FROM AppUser u WHERE u.createdAt > :date")
    long countUsersRegisteredAfter(LocalDateTime date);

    @Query("SELECT COUNT(u) FROM AppUser u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countUsersRegisteredBetween(LocalDateTime startDate, LocalDateTime endDate);
}
