package com.minhvu.sso.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.time.LocalDateTime;

@RedisHash("OTP")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTP {
    @Id
    private String email;
    private String otp;
    private LocalDateTime expirationTime;

}
