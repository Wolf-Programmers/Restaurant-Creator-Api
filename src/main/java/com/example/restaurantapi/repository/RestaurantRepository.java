package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findByNameContaining(String name);
    List<Restaurant> findByCity(String city);


}
