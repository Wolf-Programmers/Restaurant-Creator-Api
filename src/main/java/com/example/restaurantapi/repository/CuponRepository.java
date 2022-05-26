package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.Cupon;
import com.example.restaurantapi.model.Employee;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuponRepository  extends JpaRepository<Cupon, Integer> {
    Optional<Cupon> findByCuponCode(String cuponCode);
    List<Cupon> findCuponByRestaurant(Restaurant restaurant);
}
