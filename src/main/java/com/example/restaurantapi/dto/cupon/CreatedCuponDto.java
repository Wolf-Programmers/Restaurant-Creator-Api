package com.example.restaurantapi.dto.cupon;

import com.example.restaurantapi.model.Cupon;
import lombok.Data;

@Data
public class CreatedCuponDto {
    String restaurantName;
    double value;
    int maxUse;
    int id;

    public static CreatedCuponDto of(Cupon dto) {
        CreatedCuponDto coupon = new CreatedCuponDto();
        coupon.setMaxUse(dto.getMaxUses());
        coupon.setValue(dto.getValue());
        coupon.setRestaurantName(dto.getRestaurant().getName());
        coupon.setId(dto.getId());
        return coupon;
    }
}
