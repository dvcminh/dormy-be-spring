package com.minhvu.sso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendOTPEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }

    @Async
    public void sendCustomerOrderDetailLinkEmail(String email, String subjectEmail, String order_id) {
        String text = "Your order has been created successfully, here are you links to track your order:" +
                "http://localhost:5174/order/" + order_id;

        sendEmail(email, subjectEmail, text);
    }

    @Async
    public void sendAdminOrderDetailLinkEmail(String email, String subjectEmail, String order_id) {
        String text = "Your order has been created successfully, here are you links to track your order:" +
                "http://localhost:5173/order/" + order_id;

        sendEmail(email, subjectEmail, text);
    }
}
