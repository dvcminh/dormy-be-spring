package com.minhvu.review.model;

import jakarta.persistence.*;
import lombok.*;

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
    private String role;
}
