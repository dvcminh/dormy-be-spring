package com.minhvu.sso.repository;

import com.minhvu.sso.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OTPRepository extends CrudRepository<OTP, String> {
    Optional<OTP> findByEmail(String email);
}
