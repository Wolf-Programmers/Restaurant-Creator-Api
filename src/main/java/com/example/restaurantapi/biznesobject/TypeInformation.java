package com.example.restaurantapi.biznesobject;

import com.example.restaurantapi.model.ItemType;
import com.example.restaurantapi.model.MenuType;
import lombok.Data;

@Data
public class TypeInformation {
    public int id;
    public String name;


    public static TypeInformation of(MenuType t) {
        TypeInformation dto = new TypeInformation();

        dto.setId(t.getId());
        dto.setName(t.getName());
        return dto;
    }

    public static TypeInformation of(ItemType t) {
        TypeInformation dto = new TypeInformation();

        dto.setId(t.getId());
        dto.setName(t.getName());
        return dto;
    }
}
