package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.Units;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitsRepository extends JpaRepository<Units, Integer> {
}
