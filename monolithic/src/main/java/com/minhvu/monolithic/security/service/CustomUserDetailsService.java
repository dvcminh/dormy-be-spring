package com.minhvu.monolithic.security.service;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.UserCredential;
import com.minhvu.monolithic.repository.AppUserRepository;
import com.minhvu.monolithic.repository.UserCredentialRepository;
import com.minhvu.monolithic.security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    @Transactional
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email [" + username + "] not found"));
        UserCredential userCredential = userCredentialRepository.findByUserId(user.getId()).get();
        return new SecurityUser(user, userCredential);
    }
}
