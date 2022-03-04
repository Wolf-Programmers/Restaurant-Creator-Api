package com.example.restaurantapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "menu_type")
public class MenuType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;


    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="menu_id")
    private List<Menu> menus;
}
