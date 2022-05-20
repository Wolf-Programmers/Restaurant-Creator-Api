package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.order.PlaceOrderDto;
import com.example.restaurantapi.dto.order.UpdateOrderStatusDto;
import com.example.restaurantapi.service.OrderService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PutMapping("/place-order")
    public ServiceReturn placeOrder(@RequestBody PlaceOrderDto dto) {
        return orderService.placeOrder(dto);
    }

    @GetMapping("/orders")
    public ServiceReturn getOrdersRestaurant(@RequestParam("id") int id) {
        return orderService.getOrdersForRestaurant(id);
    }
    @GetMapping("/orders-by-status")
    public ServiceReturn getOrdersRestaurantByStatus(@RequestParam("restaurantId") int restaurantId, @RequestParam("statusId") int statusId) {
        return orderService.getOrdersForRestaurantByStatus(restaurantId, statusId);
    }

    @GetMapping("/order")
    public ServiceReturn getOrder(@RequestParam("id") int id) {
        return orderService.getOrder(id);
    }

    @PutMapping("/status-update")
    public ServiceReturn updateOrderStatus(@RequestBody UpdateOrderStatusDto dto) {
        return orderService.updateOrderStatus(dto);
    }
}
