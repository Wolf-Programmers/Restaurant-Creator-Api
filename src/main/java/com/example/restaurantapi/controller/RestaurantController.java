package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.restaurant.AddRestaurantDto;
import com.example.restaurantapi.service.RestaurantService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * @Author Szymon Królik
 */
@RequestMapping("/restaurant")
@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    @PostMapping("/add")
    public ServiceReturn addRestaurant(@RequestBody AddRestaurantDto dto) throws ParseException {
        return restaurantService.addRestaurantToUserAccount(dto);
    }
}
