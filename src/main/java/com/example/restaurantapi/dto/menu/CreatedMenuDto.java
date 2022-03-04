package com.example.restaurantapi.dto.menu;

import com.example.restaurantapi.model.Menu;
import com.example.restaurantapi.model.MenuType;
import com.example.restaurantapi.model.User;
import lombok.Data;

@Data
public class CreatedMenuDto {
    public String name;
    public String menuType;

    public static CreatedMenuDto of(Menu menu) {
        CreatedMenuDto dto = new CreatedMenuDto();
        dto.setName(menu.getName());
        dto.setMenuType(menu.getMenuType().getName());

        return dto;
    }
}
