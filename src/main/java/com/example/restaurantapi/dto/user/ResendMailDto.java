package com.example.restaurantapi.dto.user;

import lombok.Data;

/**
 * @Author Szymon Królik
 */
@Data
public class ResendMailDto {
    public String email;
    public String password;
}

