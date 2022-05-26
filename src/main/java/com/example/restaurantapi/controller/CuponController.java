package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.service.CuponService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Daniel Lezniak
 */

@RequestMapping("/cupon")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class CuponController {
    private final CuponService cuponService;

    @PostMapping("/cupon-create")
    public ServiceReturn createCupon(@RequestBody CreateCuponDto dto) {
        ServiceReturn ret = cuponService.createCupon(dto);

        return ret;
    }
    @GetMapping("/coupons")
    public ServiceReturn getAllCoupons(@RequestParam("restaurantId") int restuarantId) {
        return cuponService.getAllCoupons(restuarantId);
    }

}
