package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.menu.AddItemToMenuDto;
import com.example.restaurantapi.dto.menu.CreateMenuDto;
import com.example.restaurantapi.service.MenuService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Szymon Królik
 */
@RequestMapping("/menu")
@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    @PostMapping("/create")
    public ServiceReturn createMenu(@RequestBody CreateMenuDto createMenuDto) {
        return menuService.createMenu(createMenuDto);
    }

    @PostMapping("/add-item")
    public ServiceReturn addItemToMenu(@RequestBody AddItemToMenuDto addItemToMenuDto) {

        return menuService.addItemToMenu(addItemToMenuDto);
    }

    @GetMapping("/show")
    public ServiceReturn showMenuById(@RequestParam("menuId") int menuId, @RequestParam("restaurantId") int restaurantId) {
        Long menu = Long.valueOf(menuId);
        Long restaurant = Long.valueOf(restaurantId);

        return menuService.showMenuById(menu, restaurant);
    }






}
