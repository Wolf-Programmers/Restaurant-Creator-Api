package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.menu.CreateMenuDto;
import com.example.restaurantapi.service.MenuService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
