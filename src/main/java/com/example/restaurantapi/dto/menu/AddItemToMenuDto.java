package com.example.restaurantapi.dto.menu;

import lombok.Data;

@Data
public class AddItemToMenuDto {
    public int itemId;
    public int restaurantId;
    public int menuId;
}

