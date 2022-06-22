package com.example.restaurantapi.model;

import com.example.restaurantapi.service.TableService;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;

    private String fileName;

    @Lob
    private byte[] data;

    @CreationTimestamp
    @Column(updatable = false)
    private Date reportDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
