package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.restaurant.AddRestaurantDto;
import com.example.restaurantapi.dto.restaurant.InfoRestaurantDto;
import com.example.restaurantapi.dto.restaurant.UpdateRestaurantDto;
import com.example.restaurantapi.service.TableService;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;


    @Column(nullable = false)
    private String voivodeship;
    //User id FK
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    //Menu id FK
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "restaurant_menu")
    private List<Menu> menus;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Item> items;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RestaurantTable> tables;
    
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Employee> employees;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "restaurant_type_table",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_type_id")
    )
    private List<RestaurantType> restaurantTypes;

   @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "restaurant", fetch = FetchType.LAZY)
   private List<Report> reports;

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

    public static Restaurant of(InfoRestaurantDto dto) {
        Restaurant restaurant = new Restaurant();

        restaurant.setName(dto.getName());
        restaurant.setPhoneNumber(dto.getPhoneNumber());
        restaurant.setEmail(dto.getEmail());
        restaurant.setCity(dto.getCity());
        restaurant.setAddress(dto.getAddress());
        restaurant.setVoivodeship(dto.getVoivodeship());

        return restaurant;
    }

    public static Restaurant updateRestaurant(Restaurant restaurant, UpdateRestaurantDto dto) {


        restaurant.setName(dto.getName());
        restaurant.setPhoneNumber(dto.getPhoneNumber());
        restaurant.setEmail(dto.getEmail());
        restaurant.setCity(dto.getCity());
        restaurant.setAddress(dto.getAddress());
        restaurant.setVoivodeship(dto.getVoivodeship());

        return restaurant;
    }
}
