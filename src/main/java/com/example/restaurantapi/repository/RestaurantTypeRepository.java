package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.RestaurantType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTypeRepository extends JpaRepository<RestaurantType, Integer> {

}
