package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.item.CreateItemDto;
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
    public String code;
    @Column(nullable = false)
    public String maxUses;
    @Column(nullable = false)
    public Long restaurantId;
}
