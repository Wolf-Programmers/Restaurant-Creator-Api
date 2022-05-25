package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.model.Menu;
import com.example.restaurantapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {


}
