package com.example.restaurantapi.model;

import com.example.restaurantapi.service.TableService;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "employee_role")
public class EmployeeRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="employee_id")
    private List<Employee> employees;
}
