package com.example.restaurantapi.dto.user;

import com.example.restaurantapi.model.User;
import lombok.Data;

@Data
public class LoggeduserDto {
    public String name;
    public String email;
    public String phoneNumber;

    public static LoggeduserDto of (User user) {
        LoggeduserDto dto = new LoggeduserDto();

        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());

        return dto;
    }
}
