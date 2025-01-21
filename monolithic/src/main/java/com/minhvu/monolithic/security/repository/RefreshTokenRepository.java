package com.minhvu.monolithic.security.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.security.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser_Id(UUID id);

    void deleteByUser(AppUser user);
}
