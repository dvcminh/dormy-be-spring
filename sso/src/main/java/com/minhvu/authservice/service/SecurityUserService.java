package com.minhvu.authservice.service;

import com.minhvu.authservice.entity.AppUser;
import com.minhvu.authservice.entity.SecurityUser;
import com.minhvu.authservice.entity.UserCredential;
import com.minhvu.authservice.repository.UserCredentialsRepository;
import com.minhvu.authservice.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    @Transactional
    public SecurityUser loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email [" + email + "] not found");
        }
        UserCredential userCredential = userCredentialsRepository.findByUserId(user.getId()).get();
        return new SecurityUser(user, userCredential);
    }
}
