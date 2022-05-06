package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.Employee;
import com.example.restaurantapi.model.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Integer> {
}
