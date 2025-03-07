package com.minhvu.monolithic.service;

import com.minhvu.monolithic.dto.request.ChangePasswordRequest;
import com.minhvu.monolithic.exception.InvalidOldPasswordException;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.UserCredential;
import com.minhvu.monolithic.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${app.password.default}")
    private String defaultPassword;

    @Override
    public void changePassword(AppUser currentUser, ChangePasswordRequest passwordRequest) {
        isValidOldPassword(currentUser, passwordRequest);
        setPassword(currentUser.getId(), passwordRequest.getNewPassword());
    }

    @Override
    public void setPassword(UUID userId, String password) {
        UserCredential userCredential = userCredentialRepository.findById(userId).orElse(
                new UserCredential()
        );
        userCredential.setUserId(userId);
        userCredential.setPassword(passwordEncoder.encode(password));
        userCredentialRepository.saveAndFlush(userCredential);
    }

    @Override
    public void setPassword(UUID userId) {
        setPassword(userId, defaultPassword);
    }

    private void isValidOldPassword(AppUser currentUser, ChangePasswordRequest passwordRequest) {
        UserCredential userCredential = userCredentialRepository.findByUserId(currentUser.getId()).orElse(new UserCredential());
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), userCredential.getPassword())) {
            throw new InvalidOldPasswordException("Did not match current password");
        }
    }
}
