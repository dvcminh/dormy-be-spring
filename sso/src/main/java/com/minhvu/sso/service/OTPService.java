package com.minhvu.sso.service;

import com.minhvu.sso.exception.NotFoundException;
import com.minhvu.sso.model.OTP;
import com.minhvu.sso.repository.OTPRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OTPService {
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 5;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private EmailService emailService;

    public String generateOTP(String email) {
        String otp = generateRandomOTP();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES);

        OTP otpEntity = OTP.builder()
                .email(email)
                .otp(otp)
                .expirationTime(expirationTime)
                .build();
        otpRepository.save(otpEntity);
        log.info("Saved OTP: " + otpEntity);

        emailService.sendOTPEmail(email, otp);

        return otp;
    }

    public boolean validateOTP(String email, String otp) {
        if (email == null || otp == null || email.isEmpty() || otp.isEmpty()) {
            throw new IllegalArgumentException("Email and OTP must not be null or empty");
        }

        OTP otpEntity = otpRepository.findById(email).orElseThrow(() -> new NotFoundException("OTP not found"));
        log.info("Retrieved OTP: " + otpEntity);

        if (LocalDateTime.now().isAfter(otpEntity.getExpirationTime())) {
            otpRepository.delete(otpEntity);
            throw new IllegalArgumentException("OTP expired");
        }

        if (otpEntity.getOtp().equals(otp)) {
            otpRepository.delete(otpEntity);
            return true;
        }
        return false;
    }

    private String generateRandomOTP() {
        return new SecureRandom()
                .ints(OTP_LENGTH, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }


}

