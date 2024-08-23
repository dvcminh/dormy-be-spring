package com.minhvu.authservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.ChangePasswordRequest;
import com.minhvu.authservice.entity.UserCredential;
import com.minhvu.authservice.exception.InvalidOldPasswordException;
import com.minhvu.authservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserCredentialsServiceImpl implements UserCredentialsService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final UserMapper userMapper;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.password.default}")
    private String defaultPassword;

    @Override
    public boolean isEnabled(UUID userId) {
        return userCredentialsRepository.findByUserId(userId).orElse(new UserCredential()).isEnabled();
    }

    @Override
    public void changePassword(AppUserDto currentUser, ChangePasswordRequest passwordRequest) {
        isValidOldPassword(currentUser, passwordRequest);
        setPassword(currentUser.getId(), passwordRequest.getNewPassword());
    }

    @Override
    public void setPassword(UUID uuid, String password) {
        UserCredential userCredential = userCredentialsRepository.findById(uuid).orElse(
                new UserCredential()
        );
        userCredential.setUserId(uuid);
        userCredential.setPassword(passwordEncoder.encode(password));
        userCredentialsRepository.saveAndFlush(userCredential);
    }

    private void isValidOldPassword(AppUserDto currentUser, ChangePasswordRequest passwordRequest) {
        UserCredential userCredential = userCredentialsRepository.findByUserId(currentUser.getId()).orElse(new UserCredential());
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), userCredential.getPassword())) {

            throw new InvalidOldPasswordException("Did not match current password");
        }
    }
}
