package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByRestaurantId(int id);
    Optional<Employee> findEmployeeByEmail(String email);
    Optional<Employee> findEmployeeByPhoneNumber(String phoneNumber);
}
