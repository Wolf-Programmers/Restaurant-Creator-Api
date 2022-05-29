package com.example.restaurantapi.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "units")
public class Units {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;
}
