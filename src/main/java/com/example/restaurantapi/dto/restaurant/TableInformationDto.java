package com.example.restaurantapi.dto.restaurant;

import com.example.restaurantapi.model.RestaurantTable;
import lombok.Data;

import javax.persistence.Table;

@Data
public class TableInformationDto {
    private int id;
    private String number;
    private int capacity;
    private int restaurantId;

    public static TableInformationDto of(RestaurantTable table) {
        TableInformationDto dto = new TableInformationDto();

        dto.setId(table.getId());
        dto.setNumber(table.getNumber());
        dto.setCapacity(table.getCapacity());
        dto.setRestaurantId(table.getRestaurant().getId());

        return dto;
    }
}
