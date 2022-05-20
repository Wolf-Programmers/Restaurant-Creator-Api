package com.example.restaurantapi.dto.menu;

import com.example.restaurantapi.biznesobject.ItemMenuInformation;
import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.Menu;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MenuItemsDto {
    public String restaurantName;
    public String menuName;
    public String menuTypeName;
    public List<ItemMenuInformation> itemsList;


    public static MenuItemsDto of(Menu menu) {
        MenuItemsDto dto = new MenuItemsDto();
        List<ItemMenuInformation> items = new ArrayList<>();
        dto.setRestaurantName(menu.getRestaurant_menu().getName());
        dto.setMenuName(menu.getName());
        dto.setMenuTypeName(menu.getMenuType().getName());
        dto.setItemsList(menu.getMenuItems().stream()
        .map(x -> ItemMenuInformation.of(x)).collect(Collectors.toList()));

        return dto;
    }
}

