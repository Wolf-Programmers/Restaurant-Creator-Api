package com.example.restaurantapi.dto.user;

import com.example.restaurantapi.dto.restaurant.InfoRestaurantDto;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
        List<InfoRestaurantDto> tmp = new ArrayList<>();
        for (Restaurant restaurant : restaurantsList) {
            tmp.add(InfoRestaurantDto.of(restaurant));
        }
        dto.setRestaurantList(tmp);

        return dto;
    }
}
