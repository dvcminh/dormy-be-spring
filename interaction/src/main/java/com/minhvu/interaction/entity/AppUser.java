package com.minhvu.interaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class AppUser {
    @Id
    private UUID id;
    private String email;
    private String name;
    private String phone_number;
    private String address;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Date createdAt;
    private Date updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}

