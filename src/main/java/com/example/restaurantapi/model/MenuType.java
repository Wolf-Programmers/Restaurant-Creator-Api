package com.example.restaurantapi.model;

import com.example.restaurantapi.service.TableService;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "menu_type")
public class MenuType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;


    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="menu_id")
    private List<Menu> menus;
}
