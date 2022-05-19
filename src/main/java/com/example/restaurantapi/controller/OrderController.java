package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.order.PlaceOrderDto;
import com.example.restaurantapi.service.OrderService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PutMapping("/place-order")
    public ServiceReturn placeOrder(@RequestBody PlaceOrderDto dto) {
        return orderService.placeOrder(dto);
    }
}
