package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.BlackList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    Optional<BlackList> findByBlockerAndBlocked(AppUser blocker, AppUser blocked);

    boolean existsByBlockerAndBlocked(AppUser blocker, AppUser blocked);

    void deleteByBlockerAndBlocked(AppUser blocker, AppUser blocked);

    Page<BlackList> findAllByBlockerId(UUID blockerId, Pageable pageable);
}
