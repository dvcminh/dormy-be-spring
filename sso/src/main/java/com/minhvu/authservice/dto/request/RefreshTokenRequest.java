package com.minhvu.authservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotEmpty(message = "refresh token may not be empty")
    private String refreshToken;
}
