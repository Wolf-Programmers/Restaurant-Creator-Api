package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.Order;
import com.example.restaurantapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByRestaurant(Restaurant restaurant);
}
