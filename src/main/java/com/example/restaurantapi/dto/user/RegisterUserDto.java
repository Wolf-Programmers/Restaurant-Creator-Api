package com.example.restaurantapi.dto.user;

import lombok.Data;

@Data
public class RegisterUserDto {


    private String name;
    private String email;
    private String password;
    private String phone_number;
    private String matchingPassword;
}
