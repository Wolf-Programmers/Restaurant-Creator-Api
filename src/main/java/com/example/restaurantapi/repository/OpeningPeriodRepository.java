package com.example.restaurantapi.repository;


import com.example.restaurantapi.model.OpeningPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningPeriodRepository extends JpaRepository<OpeningPeriod, Long> {
}
