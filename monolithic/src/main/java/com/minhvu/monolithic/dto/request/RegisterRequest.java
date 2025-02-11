package com.minhvu.monolithic.dto.request;

import com.minhvu.monolithic.enums.Gender;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String displayName;
    private String profile;
    private Gender gender;
}
