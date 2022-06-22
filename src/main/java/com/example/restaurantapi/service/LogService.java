package com.example.restaurantapi.service;

import com.example.restaurantapi.model.Log;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    public  void saveErrorLogInDatabase(String message, int status) {
        Log log = new Log();
        log.setMessage(message);
        log.setStatus(status);
        try {
            Log createdLog = logRepository.save(log);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public void saveInfoLogInDatabase(String message, User user,  int status) {
        Log log = new Log();
        log.setMessage(message);
        log.setUser(user);
        log.setStatus(status);

        try {
            Log createdLog = logRepository.save(log);
        } catch (Exception ex) {
            saveErrorLogInDatabase(ex.getMessage(), -1);
        }
    }


}
