package com.minhvu.authservice.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String avatar;
}