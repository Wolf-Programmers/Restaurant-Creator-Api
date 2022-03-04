package com.example.restaurantapi.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String describe;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;




    //Menu FK
    @ManyToOne
    private Menu menu;
    //Type FK
    @ManyToOne
    private ItemType itemType;
    //Unit fk
    @ManyToOne
    @JoinColumn(name = "item_unit", nullable = false)
    private Unit item_unit;
}
