package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    List<AppUser> findByUsernameIgnoreCaseContainingOrDisplayNameIgnoreCaseContaining(String query, String query1);

}
