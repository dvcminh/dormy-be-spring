package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByUsername(String username);

    List<AppUser> findByIsBannedTrueAndRole(RoleType role);

    // For active users (isBanned = false OR null)
    @Query("SELECT u FROM AppUser u WHERE (u.isBanned = false OR u.isBanned IS NULL) AND u.role = :role")
    List<AppUser> findActiveUsersByRole(@Param("role") RoleType role);

    Boolean existsByUsername(String username);

    Page<AppUser> findAllByRole(RoleType role, Pageable pageable);

    List<AppUser> findByDisplayNameContainsIgnoreCaseOrderByDisplayNameAsc(String displayName);
}
