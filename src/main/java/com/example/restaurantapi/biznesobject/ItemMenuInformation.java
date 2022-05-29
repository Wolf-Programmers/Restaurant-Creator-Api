package com.example.restaurantapi.biznesobject;

import com.example.restaurantapi.model.Item;
import lombok.Data;

/**
 * @author Szymon Królik
 */
@Data
public class ItemMenuInformation {
    public int id;
    public String title;
    public String describe;
    public Double quantity;
    public String unit;
    public Double price;

    public static ItemMenuInformation of(Item item) {
        ItemMenuInformation dto = new ItemMenuInformation();

        dto.setTitle(item.getTitle());
        dto.setDescribe(item.getItem_describe());
        dto.setQuantity(item.getQuantity());
        dto.setUnit(item.getUnit());
        dto.setPrice(item.getPrice());
        dto.setId(item.getId());

        return dto;
    }
}
