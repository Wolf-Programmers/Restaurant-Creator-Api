package com.example.restaurantapi.model;

import com.example.restaurantapi.service.TableService;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "voivodeship")
public class Voivodeship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;
}
