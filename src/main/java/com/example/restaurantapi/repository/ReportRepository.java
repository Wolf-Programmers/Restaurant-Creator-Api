package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}
