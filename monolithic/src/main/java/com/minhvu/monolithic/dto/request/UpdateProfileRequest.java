package com.minhvu.monolithic.dto.request;

import com.minhvu.monolithic.enums.Gender;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String bio;
    private String profilePicture;
    private String displayName;
    private Gender gender;
}
