package com.minhvu.authservice.repository;

import com.minhvu.authservice.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByName(String name);

    boolean existsByNameAllIgnoreCase(String name);

    AppUser findByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);
}
