package com.example.restaurantapi.controller;

import com.example.restaurantapi.service.ReportService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/report")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/order")
    public ServiceReturn getOrderReport(@RequestParam("restaurantId") int restaurantId) {
        return reportService.orderReports(restaurantId);
    }
}
