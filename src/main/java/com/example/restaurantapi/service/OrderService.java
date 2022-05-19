package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.order.PlaceOrderDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderService {
    List<Map<String, String>> errList = new ArrayList<>();

    public ServiceReturn placeOrder(PlaceOrderDto dto) {
        ServiceReturn ret = new ServiceReturn();

        return ret;
    }
}
