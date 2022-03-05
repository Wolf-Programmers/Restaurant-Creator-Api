package com.example.restaurantapi.repository;

import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTitleContaining(String name);
}
