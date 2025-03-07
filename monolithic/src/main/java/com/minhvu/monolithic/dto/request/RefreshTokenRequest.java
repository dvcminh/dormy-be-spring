package com.minhvu.monolithic.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotEmpty(message = "refresh token may not be empty")
    private String refreshToken;
}
