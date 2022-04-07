package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.item.CreateItemDto;
import com.example.restaurantapi.service.ItemService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Szymon Królik
 */
@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @PutMapping("/create")
    public ServiceReturn createItem(@RequestBody CreateItemDto dto) {
        return itemService.createItem(dto);
    }

    @GetMapping("/info")
    public ServiceReturn getItemByIdInRestaurant(@RequestParam("itemId") int itemId, @RequestParam("restaurantId") int restaurantId ) {


       return itemService.findItemByIdInRestaurant(itemId, restaurantId);
    }

    @GetMapping("/info-name")
    public ServiceReturn getItemByNameInRestaurant(@RequestParam("itemName") String itemName, @RequestParam("restaurantId") int restaurantId) {


        return itemService.findItemByNameInRestaurant(itemName, restaurantId);
    }

    @GetMapping("/info-type")
    public ServiceReturn getItemByTypeInRestaurant(@RequestParam("itemType") int itemType, @RequestParam("restaurantId") int restaurantId) {

        return itemService.findItemsByTypeInRestaurant(itemType, restaurantId);
    }


}
