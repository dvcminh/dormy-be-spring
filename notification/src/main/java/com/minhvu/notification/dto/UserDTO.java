package com.minhvu.notification.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;

    private String username;

    private String password;

    private String email;

    private String firstname;

    private String lastname;

    private Date dateOfBirth;
    private String country;
    private LocalDateTime createdAt;
}
