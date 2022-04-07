package com.example.restaurantapi.biznesobject;

import com.example.restaurantapi.model.RestaurantType;
import lombok.Data;

@Data
public class RestaurantTypes {
    private int id;
    private String name;

    public static RestaurantTypes of(RestaurantType restaurantType) {
        RestaurantTypes dto = new RestaurantTypes();

        dto.setId(restaurantType.getId());
        dto.setName(restaurantType.getName());

        return dto;
    }
}
