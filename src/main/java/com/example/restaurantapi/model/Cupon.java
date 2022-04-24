package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.item.CreateItemDto;
import com.example.restaurantapi.dto.restaurant.AddEmployeeDto;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public static Cupon of(CreateCuponDto dto) {
        Cupon cupon = new Cupon();
        cupon.setCuponCode(dto.getCuponCode());
        cupon.setMaxUses(dto.getMaxUse());
        cupon.setRestaurant(dto.getRestaurant());
        return cupon;
    }
}
