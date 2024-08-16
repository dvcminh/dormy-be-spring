package com.minhvu.friend.dto;

import com.minhvu.friend.model.entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private String phone_number;
    private String address;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;
}
