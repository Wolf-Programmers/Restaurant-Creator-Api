package com.example.restaurantapi.dto.restaurant;

import com.example.restaurantapi.model.Restaurant;
import lombok.Data;

@Data
public class CreateTableDto {
    private int tableId;
    private String number;
    private int capacity;
    private int restaurantId;
    private Restaurant restaurant;

}
