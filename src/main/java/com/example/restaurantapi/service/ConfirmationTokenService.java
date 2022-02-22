package com.example.restaurantapi.service;

import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToke(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public void deleteConfirmationToken(Long confirmationToken) {
        confirmationTokenRepository.deleteById(confirmationToken);
    }
}
