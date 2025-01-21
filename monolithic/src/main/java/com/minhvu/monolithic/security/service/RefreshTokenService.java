package com.minhvu.monolithic.security.service;


import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.repository.AppUserRepository;
import com.minhvu.monolithic.security.exception.TokenRefreshException;
import com.minhvu.monolithic.security.model.RefreshToken;
import com.minhvu.monolithic.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AppUserRepository userRepository;
    @Value("${jwt.refreshExp}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void createRefreshToken(AppUser user, String token) {
        if(refreshTokenRepository.findByUser_Id(user.getId()).isPresent()) {
            refreshTokenRepository.deleteByUser(user);
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(token);

        refreshTokenRepository.saveAndFlush(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new login request");
        }

        return token;
    }
}
