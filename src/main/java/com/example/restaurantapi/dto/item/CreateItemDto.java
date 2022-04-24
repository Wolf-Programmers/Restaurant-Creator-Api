package com.example.restaurantapi.dto.item;

import com.example.restaurantapi.model.Item;
import lombok.Data;

@Data
public class CreateItemDto {
    public String title;
    public String desc;
    public Double quantity;
    public Double price;
    public String unit;
    public Long restaurantId;
    public String restaurantName;
    public int itemType;
    public String itemTypeName;

    public static CreateItemDto of (Item item) {
        CreateItemDto dto = new CreateItemDto();

        dto.setTitle(item.getTitle());
        dto.setDesc(item.getItem_describe());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setUnit(item.getUnit());


        return dto;
    }
}
