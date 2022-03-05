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
    private Long id;

    @Column(nullable = false)
    private String name;

    //FK restaurant
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant_menu;

    //FK menu type
    @ManyToOne
    private MenuType menuType;

    public static Menu of(CreateMenuDto createMenuDto) {
        Menu menu = new Menu();

        menu.setName(createMenuDto.getName());
        menu.setMenuType(createMenuDto.getMenuType());

        return menu;
    }
}
