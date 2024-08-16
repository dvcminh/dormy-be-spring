package com.minhvu.authservice.service;

import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.RegisterRequest;
import com.minhvu.authservice.dto.UpdateUserInformationRequest;
import com.minhvu.authservice.entity.AppUser;
import com.minhvu.authservice.entity.Role;
import com.minhvu.authservice.entity.SecurityUser;
import com.minhvu.authservice.exception.BadRequestException;
import com.minhvu.authservice.exception.UserNotFoundException;
import com.minhvu.authservice.exception.response.UserErrorResponse;
import com.minhvu.authservice.mapper.UserMapper;
import com.minhvu.authservice.repository.UserCredentialsRepository;
import com.minhvu.authservice.repository.UserCredentialsService;
import com.minhvu.authservice.repository.AppUserRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.Key;
import java.util.Date;


@Service
@Data
public class AuthService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserCredentialsService userCredentialService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String jwtSecret;
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(SecurityUser userDetails, Long time) {
        String username = userDetails.getUsername();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + time);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public AppUserDto signUp(RegisterRequest registerRequest) {
        if (appUserRepository.existsByEmailIgnoreCase(registerRequest.getEmail())) {
            throw new BadRequestException("Username is already taken!");
        }

        AppUser newUser = appUserRepository.save(
                AppUser.builder()
                        .name(registerRequest.getName())
                        .email(registerRequest.getEmail())
                        .address(registerRequest.getAddress())
                        .phone_number(registerRequest.getPhone_number())
                        .avatar(registerRequest.getAvatar())
                        .role(Role.CUSTOMER)
                        .build()
        );

        userCredentialService.setPassword(newUser.getId(), registerRequest.getPassword());
        return userMapper.toDto(newUser);
    }

}
