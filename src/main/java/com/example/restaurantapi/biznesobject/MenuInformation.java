package com.example.restaurantapi.biznesobject;

import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuInformation {
    public List<ItemMenuInformation> itemList;
    public String name;

    public static MenuInformation of (Menu menu) {
        MenuInformation dto = new MenuInformation();
        List<ItemMenuInformation> itemMenuInformations = new ArrayList<>();
        for (Item item : menu.getMenuItems()) {
            ItemMenuInformation itemMenuInformation = ItemMenuInformation.of(item);
            itemMenuInformations.add(itemMenuInformation);
        }
        dto.setItemList(itemMenuInformations);
        dto.setName(menu.getName());

        return dto;
    }
}
