package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.employee.AddEmployeeDto;
import com.example.restaurantapi.dto.employee.EmployeeInformationDto;
import com.example.restaurantapi.service.TableService;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Double salary;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    private EmployeeRole employeeRole;

    public static Employee of(AddEmployeeDto dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setLastName(dto.getLastName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());


        return employee;
    }

    public static Employee of(EmployeeInformationDto dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setLastName(dto.getLastName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());
        employee.setEmployeeRole(dto.getEmployeeRoleModel());
        employee.setPassword(dto.getPassword());
        return employee;
    }

     public static Employee updateEmployee(Employee employee, EmployeeInformationDto dto) {

        employee.setName(dto.getName());
        employee.setLastName(dto.getLastName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());
        employee.setEmployeeRole(dto.getEmployeeRoleModel());
        employee.setPassword(dto.getPassword());
        employee.setRestaurant(dto.getEmployeeRestaurantModel());

        return employee;
    }
}
