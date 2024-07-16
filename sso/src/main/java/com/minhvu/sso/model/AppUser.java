package com.minhvu.sso.model;

import com.minhvu.sso.model.enums.AuthorityType;
import com.minhvu.sso.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class AppUser extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    @Column(length = 10485760)
    private String avatar;

    @Enumerated(EnumType.STRING)
    private AuthorityType authority;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "user")
    private Collection<UserComponent> components;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCredential userCredential;
}
