package com.example.restaurantapi.biznesobject;

import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MenuInformation {
    public List<ItemMenuInformation> itemList;
    public String name;

    public static MenuInformation of (Menu menu) {
        MenuInformation dto = new MenuInformation();
        dto.setItemList(menu.getMenuItems().stream()
        .map(x -> ItemMenuInformation.of(x)).collect(Collectors.toList()));
        dto.setName(menu.getName());

        return dto;
    }
}
