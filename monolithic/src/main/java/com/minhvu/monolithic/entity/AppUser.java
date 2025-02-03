package com.minhvu.monolithic.entity;

import com.minhvu.monolithic.entity.enums.RoleType;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser extends BaseEntity {
    private String username;
    private String displayName;
    private String profilePicture;
    private String bio;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private RoleType role;
    private Boolean isBanned = false;
}
