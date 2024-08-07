package com.minhvu.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    @Column(nullable = false, unique = true, length = 100000)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

}
