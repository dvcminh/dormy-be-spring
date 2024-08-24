package com.minhvu.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
@Builder
public class AppUser extends BaseEntity {

    private String email;
    private String name;
    private String phone;
    private String address;
    private String avatar;

    @Enumerated(EnumType.STRING)
    private Role role;
}
