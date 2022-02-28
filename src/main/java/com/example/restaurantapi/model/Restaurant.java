package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.restaurant.AddRestaurantDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
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

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;


    @Column(nullable = false)
    private String voivodeship;
    //User id FK
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    //Menu id FK
    @OneToMany(mappedBy = "restaurant_menu")
    private Set<Menu> menus;


    public static Restaurant of(AddRestaurantDto dto) {
        Restaurant restaurant = new Restaurant();

        restaurant.setName(dto.getName());
        restaurant.setPhoneNumber(dto.getPhoneNumber());
        restaurant.setEmail(dto.getEmail());
        restaurant.setCity(dto.getCity());
        restaurant.setAddress(dto.getAddress());
        restaurant.setVoivodeship(dto.getVoivodeship());

        return restaurant;


    }
}
