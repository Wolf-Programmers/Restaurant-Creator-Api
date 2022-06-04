package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.restaurant.AddRestaurantDto;
import com.example.restaurantapi.dto.restaurant.UpdateRestaurantDto;
import com.example.restaurantapi.service.RestaurantService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @Author Szymon Królik
 */
@RequestMapping("/restaurant")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * @return
     */
    @GetMapping("/get-types")
    public ServiceReturn getResturantTypes() {
        return restaurantService.getRestaurantTypes();
    }

    @PutMapping("/add")
    public ServiceReturn addRestaurant(@RequestBody AddRestaurantDto dto) throws ParseException {
        return restaurantService.addRestaurantToUserAccount(dto);
    }

    @PostMapping("/update")
    public ServiceReturn updateResturant(@RequestBody UpdateRestaurantDto dto) throws ParseException {
        return restaurantService.updateRestaurant(dto);
    }

    @GetMapping("/info")
    public ServiceReturn getRestaurant(@RequestParam("id") int id) {

        return restaurantService.getRestaurant(id);
    }

    @GetMapping("/info-name")
    public ServiceReturn getRestaurant(@RequestParam("name") String name) {
        return restaurantService.getRestaurantByName(name);
    }

    @GetMapping("/info-city")
    public ServiceReturn getRestaurantByCity(@RequestParam("city") String city) {
        return restaurantService.getRestaurantsByCity(city);
    }

    @GetMapping("/get-restaurants")
    public ServiceReturn getRestaurantByOwner(@RequestParam("ownerId") int ownerId) {
        return restaurantService.getResturantsByOwner(ownerId);
    }

    @DeleteMapping("/delete")
    public ServiceReturn deleteRestaurant(@RequestParam("restaurantId") int restaurantId) {
        return restaurantService.deleteRestaurant(restaurantId);
    }

    @GetMapping("/get-voivodeship")
    public ServiceReturn getVoivodeships() {
        return restaurantService.getVoivodeships();
    }

}
