package com.example.restaurantapi.dto.restaurant;

import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.model.Restaurant;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author Szymon Królik
 */
@Data
public class AddRestaurantDto {
    private Long owner;
    private String name;
    private String city;
    private String address;
    private String phoneNumber;
    private String email;
    public String voivodeship;
    private List<OpeningTimes> openingTimes;



}
