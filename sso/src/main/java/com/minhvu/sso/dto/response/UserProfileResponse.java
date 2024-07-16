package com.minhvu.sso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String authority;
    private String role;
    private String avatar;
    private Date createdAt;
    private Date updatedAt;
    private boolean isEnabled;
    private String planName;
    private Long planExpiredIn;
}
