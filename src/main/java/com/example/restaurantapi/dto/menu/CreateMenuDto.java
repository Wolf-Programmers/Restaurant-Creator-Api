package com.example.restaurantapi.dto.menu;

import com.example.restaurantapi.model.MenuType;
import lombok.Data;

@Data
public class CreateMenuDto {
    public int creatorId;
    public String name;
    public int menuTypeId;
    public int restaurantId;
    public MenuType menuType;



}
