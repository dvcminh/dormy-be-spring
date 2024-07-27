package com.minhvu.sso.controller;

import com.minhvu.sso.dto.request.GenerateOtpRequest;
import com.minhvu.sso.dto.request.OTPValidationRequest;
import com.minhvu.sso.service.OTPService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/otp")
public class OTPController {
    @Autowired
    private OTPService otpService;

    private final Bucket bucket;

    public OTPController() {
        Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateOTP(@RequestBody GenerateOtpRequest email) {
        if (bucket.tryConsume(1)) {
            String otp = otpService.generateOTP(email.getEmail());
            return ResponseEntity.ok("OTP sent to your email");
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded. Try again later.");
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateOTP(@RequestBody OTPValidationRequest request) {
        boolean isValid = otpService.validateOTP(request.getEmail(), request.getOtp());
        if (isValid) {
            return ResponseEntity.ok("OTP validated successfully");
        }
        return ResponseEntity.badRequest().body("Invalid OTP");
    }
}
