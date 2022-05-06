package com.example.restaurantapi.dto.employee;

import com.example.restaurantapi.model.Employee;
import lombok.Data;

@Data
public class EmployeeInformationDto {
    public int id;
    public String name;
    public String lastName;
    public String phoneNumber;
    public String email;
    public double salary;

    public static EmployeeInformationDto of(Employee employee) {
        EmployeeInformationDto dto = new EmployeeInformationDto();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setLastName(employee.getLastName());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setEmail(employee.getEmail());
        dto.setSalary(employee.getSalary());

        return dto;
    }
}
