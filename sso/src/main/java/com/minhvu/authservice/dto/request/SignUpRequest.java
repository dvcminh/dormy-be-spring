package com.minhvu.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {

    @NotEmpty(message = "First name may not be empty")
    private String name;

    @Email
    @NotEmpty(message = "Email may not be empty")
    private String email;

    @NotEmpty(message = "Password may not be empty")
    private String password;
}
