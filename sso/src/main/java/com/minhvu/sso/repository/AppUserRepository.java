package com.minhvu.sso.repository;


import com.minhvu.sso.model.AppUser;
import com.minhvu.sso.model.enums.AuthorityType;
import com.minhvu.sso.model.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {



    @Query(value = "SELECT u FROM AppUser u " +
            "WHERE u.authority=:authority " +
            "AND (convertToNonSigned(u.email, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(u.phone, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(CONCAT(u.firstName, ' ', u.lastName), :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) " +
            "AND (u.createdAt BETWEEN COALESCE(:startTs, u.createdAt) AND COALESCE(:endTs, u.createdAt)) " +
            "AND (:isEnabled IS NULL OR u.userCredential.enabled = :isEnabled)"
    )
    Page<AppUser> findUsersBySysAdmin(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("authority") AuthorityType authority,
            @Param("startTs") LocalDateTime startTs,
            @Param("endTs") LocalDateTime endTs,
            @Param("isEnabled") Boolean isEnabled,
            Pageable pageable
    );

    @Query(value = "SELECT u FROM AppUser u WHERE u.email=:email")
    AppUser findByEmail(String email);

    List<AppUser> findByAuthority(AuthorityType type);

    Optional<AppUser> findByIdAndAuthority(UUID id, AuthorityType authority);

}
