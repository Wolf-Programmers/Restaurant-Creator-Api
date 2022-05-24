package com.example.restaurantapi.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

/**
 * @Author Szymon Królik
 */
@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender javaMailSender;
    public boolean finished;
    public EmailService(){};

    /**
     * @param mailMessage
     * send mail to user email
     */
    @Async
    public void sendEmail(SimpleMailMessage mailMessage) {
        javaMailSender.send(mailMessage);
        finished = true;
    }
}
