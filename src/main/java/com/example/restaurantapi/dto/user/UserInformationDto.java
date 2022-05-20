package com.example.restaurantapi.dto.user;

import com.example.restaurantapi.dto.restaurant.InfoRestaurantDto;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserInformationDto {
    public int id;
    public String name;
    public String phoneNumber;
    public String email;
    public String password;
    public List<InfoRestaurantDto> restaurantList;

    public static UserInformationDto of (User user) {
        UserInformationDto dto = new UserInformationDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        List<Restaurant> restaurantsList = user.getRestaurants();
        dto.setRestaurantList(user.getRestaurants().stream()
        .map(x -> InfoRestaurantDto.of(x)).collect(Collectors.toList()));


        return dto;
    }
}
