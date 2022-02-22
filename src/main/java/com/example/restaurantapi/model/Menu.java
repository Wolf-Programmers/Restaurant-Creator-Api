package com.example.restaurantapi.model;

import lombok.Data;

import javax.persistence.*;
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
    //FK items<>
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="item_id")
    private Set<Item> items;
    //FK menu type
    @ManyToOne
    private MenuType menuType;
}
