package com.minhvu.authservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserCredential {

    @Id
    private UUID userId;

    private boolean enabled = true;

    @JsonIgnore
    private String password;

    @OneToOne
    @JoinColumn(name = "appuser_id")
    private AppUser user;
}
