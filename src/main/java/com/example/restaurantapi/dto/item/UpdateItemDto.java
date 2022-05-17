package com.example.restaurantapi.dto.item;

import com.example.restaurantapi.model.ItemType;
import com.example.restaurantapi.model.Restaurant;
import lombok.Data;

@Data
public class UpdateItemDto {
    public int id;
    public String title;
    public String desc;
    public Double quantity;
    public Double price;
    public String unit;
    public int restaurantId;
    public String restaurantName;
    public int itemTypeId;
    public String itemTypeName;
    Restaurant restaurant;
    ItemType itemType;
}
