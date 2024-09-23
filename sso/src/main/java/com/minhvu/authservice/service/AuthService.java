package com.minhvu.authservice.service;

import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.RegisterRequest;
import com.minhvu.authservice.entity.AppUser;
import com.minhvu.authservice.entity.Role;
import com.minhvu.authservice.entity.SecurityUser;
import com.minhvu.authservice.exception.BadRequestException;
import com.minhvu.authservice.kafka.UserProducer;
import com.minhvu.authservice.mapper.UserMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;


@Service
@Data
public class AuthService {
    private static final String USER_ID = "userId";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "name";
    private static final String PHONE = "phone";
    private static final String ROLE = "role";
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
    @Autowired
    private UserProducer userProducer;

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

        JwtBuilder token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256);

        token.claim(USER_ID, userDetails.getUser().getId())
                .claim(EMAIL, userDetails.getUser().getEmail())
                .claim(FIRST_NAME, userDetails.getUser().getName())
                .claim(PHONE, userDetails.getUser().getPhone())
                .claim(ROLE, userDetails.getUser().getRole());

        return token.compact();
    }
    @Transactional
    public AppUserDto signUp(RegisterRequest registerRequest) {
        if (appUserRepository.existsByEmailIgnoreCase(registerRequest.getEmail())) {
            throw new BadRequestException("Username is already taken!");
        }

        AppUser newUser = appUserRepository.saveAndFlush(
                AppUser.builder()
                        .name(registerRequest.getName())
                        .email(registerRequest.getEmail())
                        .address(registerRequest.getAddress())
                        .phone(registerRequest.getPhone_number())
                        .avatar(registerRequest.getAvatar())
                        .role(Role.CUSTOMER)
                        .build()
        );

        userProducer.sendMessage(userMapper.toDto(newUser));
        userCredentialService.setPassword(newUser.getId(), registerRequest.getPassword());
        return userMapper.toDto(newUser);
    }

}
