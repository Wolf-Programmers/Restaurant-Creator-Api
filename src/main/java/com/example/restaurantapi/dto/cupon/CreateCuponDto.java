package com.example.restaurantapi.dto.cupon;

import com.example.restaurantapi.dto.item.CreateItemDto;
import com.example.restaurantapi.model.Cupon;
import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.Restaurant;
import lombok.Data;

@Data
public class CreateCuponDto {
    private String cuponCode;
    private int maxUse;
    private Restaurant restaurant;

    public static CreateCuponDto of (Cupon cupon) {
        CreateCuponDto dto = new CreateCuponDto();

        dto.setCuponCode(cupon.getCuponCode());
        dto.setMaxUse(cupon.getMaxUses());
        dto.setRestaurant(cupon.getRestaurant());

        return dto;
    }
}
