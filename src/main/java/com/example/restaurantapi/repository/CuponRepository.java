package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.Cupon;
import com.example.restaurantapi.model.Employee;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CuponRepository  extends JpaRepository<Cupon, Integer> {
    Optional<Cupon> findByCuponCode(String cuponCode);
//    SELECT * FROM cupon WHERE restaurant_id IN (SELECT restaurant_id FROM users WHERE id = 42);
    @Query("SELECT c FROM Cupon c WHERE c.restaurant.id IN (SELECT User.restaurants FROM User WHERE id = :ownerId)")
    List<Cupon> findCuponByOwner(int ownerId);
    List<Cupon> findCuponByRestaurant(Restaurant restaurant);
}
