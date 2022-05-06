package com.example.restaurantapi.dto.employee;

import com.example.restaurantapi.model.Employee;
import lombok.Data;

/**
 * @Author Szymon Królik
 */
@Data
public class CreatedEmployeeDto {
    public String name;
    public String lastName;
    public String phoneNumber;
    public String email;
    public Double salary;

    public static CreatedEmployeeDto of(Employee employee) {
        CreatedEmployeeDto dto = new CreatedEmployeeDto();

        dto.setName(employee.getName());
        dto.setLastName(employee.getLastName());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setEmail(employee.getEmail());
        dto.setSalary(employee.getSalary());

        return dto;
    }

}
