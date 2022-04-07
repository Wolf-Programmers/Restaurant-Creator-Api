package com.example.restaurantapi.dto.restaurant;

import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.biznesobject.RestaurantTypes;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.RestaurantType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author Szymon Królik
 */
@Data
public class AddRestaurantDto {
    private int owner;
    private String name;
    private String city;
    private String address;
    private String phoneNumber;
    private String email;
    public String voivodeship;
    private List<OpeningTimes> openingTimes;
    private List<RestaurantTypes> restaurantTypesList;



}
