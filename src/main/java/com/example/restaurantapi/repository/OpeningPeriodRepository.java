package com.example.restaurantapi.repository;


import com.example.restaurantapi.model.OpeningPeriod;
import com.example.restaurantapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpeningPeriodRepository extends JpaRepository<OpeningPeriod, Long> {
    List<OpeningPeriod> findByRestaurant(Restaurant restaurant);
}
