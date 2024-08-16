package com.minhvu.authservice.repository;

import com.minhvu.authservice.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByName(String name);

    boolean existsByNameAllIgnoreCase(String name);

    AppUser findByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);


    @Override
    boolean existsById(Long id);
}
