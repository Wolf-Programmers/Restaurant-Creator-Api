package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {
}
