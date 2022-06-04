package com.example.restaurantapi.dto.cupon;

import com.example.restaurantapi.model.Restaurant;
import lombok.Data;

@Data
public class UpdateCouponDto {

    public int id;
    public String cuponCode;
    public int maxUse;
    public int restaurantId;
    public double value;
    public Restaurant restaurant;

}
