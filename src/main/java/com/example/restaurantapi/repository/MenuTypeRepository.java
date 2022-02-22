package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.model.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuTypeRepository extends JpaRepository<MenuType, Long> {
}
