package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.cupon.UpdateCouponDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cupon")
public class Cupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String cuponCode;
    @Column(nullable = false)
    private int maxUses;
    @Column(nullable = false)
    private double value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public static Cupon of(CreateCuponDto dto) {
        Cupon cupon = new Cupon();
        cupon.setCuponCode(dto.getCuponCode());
        cupon.setMaxUses(dto.getMaxUse());
        cupon.setValue(dto.getValue());
        cupon.setRestaurant(dto.getRestaurant());
        return cupon;
    }

    public static Cupon updateCoupon(Cupon cupon) {
        cupon.setMaxUses(cupon.getMaxUses()-1);
        return cupon;
    }

    public static Cupon editCupon(Cupon cupon, UpdateCouponDto dto) {
        cupon.setMaxUses(dto.getMaxUse());
        cupon.setCuponCode(dto.getCuponCode());
        cupon.setRestaurant(dto.getRestaurant());
        cupon.setValue(dto.getValue());
        cupon.setMaxUses(dto.getMaxUse());

        return cupon;
    }



}
