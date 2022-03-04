package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.RestaurantType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTypeRepository extends JpaRepository<RestaurantType, Long> {
}
