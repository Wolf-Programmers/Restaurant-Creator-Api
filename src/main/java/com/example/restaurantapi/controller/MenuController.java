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
@CrossOrigin
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

        return menuService.showMenuById(menuId, restaurantId);
    }

    @GetMapping("/show-restaurant-menus")
    public ServiceReturn showRestaurantMenus(@RequestParam("restaurantId") int restaurantId) {
        return menuService.showRestaurantMenus(restaurantId);
    }

    @GetMapping("/show-menu-by-owner")
    public ServiceReturn showMenuByOwnerId(@RequestParam("ownerId") int ownerId) {
        return  menuService.showMenusByOwner(ownerId);
    }

    @GetMapping("/get-menu-types")
    public ServiceReturn getMenuTypes() {
        return menuService.getMenuTypes();
    }







}
