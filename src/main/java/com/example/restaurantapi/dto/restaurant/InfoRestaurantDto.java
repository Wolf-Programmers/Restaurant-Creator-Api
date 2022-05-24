package com.example.restaurantapi.dto.restaurant;

import com.example.restaurantapi.biznesobject.MenuInformation;
import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.biznesobject.RestaurantTypes;
import com.example.restaurantapi.model.Menu;
import com.example.restaurantapi.model.OpeningPeriod;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.RestaurantType;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class InfoRestaurantDto {
    private int id;
    private String ownerName;
    private String name;
    private String city;
    private String address;
    private String phoneNumber;
    private String email;
    public String voivodeship;
    private List<MenuInformation> menus;
    private List<OpeningTimes> openingPeriod;
    private List<RestaurantTypes> restaurantTypes;

    public static InfoRestaurantDto of (Restaurant restaurant) {
        InfoRestaurantDto dto = new InfoRestaurantDto();
        dto.setId(restaurant.getId());
        dto.setOwnerName(restaurant.getUser_id().getName());
        dto.setName(restaurant.getName());
        dto.setCity(restaurant.getCity());
        dto.setAddress(restaurant.getAddress());
        dto.setPhoneNumber(restaurant.getPhoneNumber());
        dto.setEmail(restaurant.getEmail());
        dto.setVoivodeship(restaurant.getVoivodeship());


        return dto;
    }
}
