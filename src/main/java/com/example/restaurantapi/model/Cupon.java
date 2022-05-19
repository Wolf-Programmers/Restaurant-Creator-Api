package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cupon")
public class Cupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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



}
