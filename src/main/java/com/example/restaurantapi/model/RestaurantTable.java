package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.restaurant.CreateTableDto;
import com.example.restaurantapi.service.TableService;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "table")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne
    private Restaurant restaurant;

    public static RestaurantTable update(RestaurantTable table, CreateTableDto dto) { ;

        table.setNumber(dto.getNumber());
        table.setRestaurant(dto.getRestaurant());
        table.setNumber(dto.getNumber());
        table.setCapacity(dto.getCapacity());

        return table;
    }

}
