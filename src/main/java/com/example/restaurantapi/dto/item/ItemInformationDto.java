package com.example.restaurantapi.dto.item;

import com.example.restaurantapi.model.Item;
import lombok.Data;

@Data
public class ItemInformationDto {
    private String title;
    private String unit;
    private double price;

    public static ItemInformationDto of(Item item) {
        ItemInformationDto dto = new ItemInformationDto();
        dto.setTitle(item.getTitle());
        dto.setPrice(item.getPrice());
        dto.setUnit(item.getUnit());

        return dto;
    }
}
