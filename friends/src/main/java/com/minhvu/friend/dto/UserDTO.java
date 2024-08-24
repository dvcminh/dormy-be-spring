package com.minhvu.friend.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private String email;
    private String name;
    private String phone_number;
    private String address;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;
}
