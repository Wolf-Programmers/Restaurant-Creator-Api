package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.restaurant.CreateTableDto;
import com.example.restaurantapi.service.ServiceReturn;
import com.example.restaurantapi.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/table")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class TableController {

    private final TableService tableService;

    @PostMapping("/create")
    public ServiceReturn createTable(@RequestBody CreateTableDto dto) {
        return tableService.createTable(dto);
    }

    @GetMapping("/get-by-id")
    public ServiceReturn getTableById(@RequestParam("tableId") int tableId) {
        return tableService.getTable(tableId);
    }

    @GetMapping("/get-by-restaurant")
    public ServiceReturn getTableByRestaurant(@RequestParam("restaurantId") int restaurantId) {
        return tableService.getTableByRestaurantId(restaurantId);
    }

    @PutMapping("/update")
    public ServiceReturn updateTable(@RequestBody CreateTableDto dto) {
        return tableService.editTable(dto);
    }
}


