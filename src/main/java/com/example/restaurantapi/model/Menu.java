package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.menu.CreateMenuDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    //FK restaurant
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant_menu;

    //FK menu type
    @ManyToOne
    private MenuType menuType;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "menu_item_table",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> menuItems;

    public static Menu of(CreateMenuDto createMenuDto) {
        Menu menu = new Menu();

        menu.setName(createMenuDto.getName());
        menu.setMenuType(createMenuDto.getMenuType());

        return menu;
    }

    public static Menu of(Menu menu) {
        Menu dto = new Menu();

        dto.setName(menu.getName());
        dto.setRestaurant_menu(menu.getRestaurant_menu());
        dto.setMenuType(menu.getMenuType());
        dto.setMenuItems(menu.getMenuItems());

        return dto;
    }
}
