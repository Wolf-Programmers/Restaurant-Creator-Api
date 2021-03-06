package com.example.restaurantapi.service;

import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author Szymon Królik
 */
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public void deleteConfirmationToken(int confirmationToken) {
        confirmationTokenRepository.deleteById(confirmationToken);
    }

    public void saveForgotPasswordToken(ConfirmationToken forgotPasswordToken) {
        confirmationTokenRepository.save(forgotPasswordToken);
    }

    public void deleteForgotPasswordToken(int forgotPasswordToken) {
        confirmationTokenRepository.deleteById(forgotPasswordToken);
    }
}
