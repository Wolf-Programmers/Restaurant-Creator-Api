package com.example.restaurantapi.model;

import com.example.restaurantapi.service.TableService;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "restaurantType")
public class RestaurantType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "restaurantTypes")
    private List<Restaurant> restaurants;
}
