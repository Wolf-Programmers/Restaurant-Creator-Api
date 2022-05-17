package com.example.restaurantapi.dto.restaurant;

import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.biznesobject.RestaurantTypes;
import com.example.restaurantapi.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRestaurantDto {
    private int restaurantId;
    private String ownerName;
    public int ownerId;
    public User owner;
    private String name;
    private String city;
    private String address;
    private String phoneNumber;
    private String email;
    public String voivodeship;
    private List<OpeningTimes> openingPeriod;
    private List<RestaurantTypes> restaurantTypes;
}
