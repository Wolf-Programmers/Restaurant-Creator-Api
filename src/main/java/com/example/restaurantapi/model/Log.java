package com.example.restaurantapi.model;

import com.example.restaurantapi.service.TableService;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "message")
    private String message;
    @Column(name = "status")
    private int status;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



}
