package com.minhvu.authservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ChangePasswordRequest {

    @NotEmpty(message = "old password may not be empty")
    @Size(min = 8, max = 255)
    private String oldPassword;

    @NotEmpty(message = "new password may not be empty")
    @Size(min = 8, max = 255)
    private String newPassword;

}
