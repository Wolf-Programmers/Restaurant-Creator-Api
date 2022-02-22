package com.example.restaurantapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    //User id FK
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User restaurant_id;

    //Menu id FK
    @OneToMany(mappedBy = "restaurant_menu")
    private Set<Menu> menus;
}
