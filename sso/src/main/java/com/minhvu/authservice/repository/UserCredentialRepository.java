package com.minhvu.authservice.repository;

import com.minhvu.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository  extends JpaRepository<User,String> {
    Optional<User> findByName(String username);

}
