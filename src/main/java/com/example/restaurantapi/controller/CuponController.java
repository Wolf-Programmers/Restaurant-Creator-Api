package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.cupon.UpdateCouponDto;
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

    @PutMapping("/cupon-create")
    public ServiceReturn createCupon(@RequestBody CreateCuponDto dto) {
        return cuponService.createCupon(dto);
    }
    @GetMapping("/coupons")
    public ServiceReturn getAllCoupons(@RequestParam("userId") int userId) {
        return cuponService.getAllCoupons(userId);
    }

    @PostMapping("/update")
    public ServiceReturn updateCoupon(@RequestBody UpdateCouponDto dto) {
        return cuponService.editCoupon(dto);
    }

    @DeleteMapping("/delete")
    public ServiceReturn deleteCoupon(@RequestParam("couponId") int couponId) {
        return cuponService.deleteCoupon(couponId);
    }


}
