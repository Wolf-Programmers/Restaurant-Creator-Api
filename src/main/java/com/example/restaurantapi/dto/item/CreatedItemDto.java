package com.example.restaurantapi.dto.item;

import com.example.restaurantapi.model.Item;
import lombok.Data;

@Data
public class CreatedItemDto {
    public String title;
    public String desc;
    public Double quantity;
    public Double price;
    public String unit;
    public int restaurantId;
    public String restaurantName;
    public int itemType;
    public String itemTypeName;

    public static CreatedItemDto of(Item item) {
        CreatedItemDto dto = new CreatedItemDto();
        dto.setTitle(item.getTitle());
        dto.setDesc(item.getItem_describe());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setUnit(item.getUnit());
        dto.setRestaurantId(item.getRestaurant().getId());
        dto.setRestaurantName(item.getRestaurant().getName());
        dto.setItemType(item.getItemType().getId());
        dto.setItemTypeName(item.getItemType().getName());

        return dto;
    }

}
