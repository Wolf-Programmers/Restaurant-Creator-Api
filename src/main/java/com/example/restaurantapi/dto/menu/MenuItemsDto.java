package com.example.restaurantapi.dto.menu;

import com.example.restaurantapi.biznesobject.ItemMenuInformation;
import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.Menu;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

        List<Item> listItems = menu.getMenuItems();
        for (Item item : listItems) {
            ItemMenuInformation x = ItemMenuInformation.of(item);
            items.add(x);
        }

        dto.setItemsList(items);

        return dto;
    }
}

