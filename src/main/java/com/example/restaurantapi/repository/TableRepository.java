package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<RestaurantTable, Integer> {
}
