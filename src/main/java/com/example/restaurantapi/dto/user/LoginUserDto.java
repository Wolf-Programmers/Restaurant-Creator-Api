package com.example.restaurantapi.dto.user;

import com.example.restaurantapi.model.User;
import lombok.Data;

@Data
public class LoginUserDto {
//    private String phoneNumber;
//    private String email;
    private String login;
    private String password;

    public static LoginUserDto of(ResendMailDto dto) {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setLogin(dto.getEmail());
        loginUserDto.setPassword(dto.getPassword());

        return loginUserDto;
    }
}
