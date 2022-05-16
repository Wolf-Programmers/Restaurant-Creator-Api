package com.example.restaurantapi.dto.employee;

import com.example.restaurantapi.dto.restaurant.InfoRestaurantDto;
import com.example.restaurantapi.model.Employee;
import com.example.restaurantapi.model.EmployeeRole;
import com.example.restaurantapi.model.Restaurant;
import lombok.Data;

@Data
public class EmployeeInformationDto {
    public int id;
    public int employeeRoleId;
    public int employeeRestaurantId;
    public String name;
    public String lastName;
    public String phoneNumber;
    public String email;
    public EmployeeRoleInformationDto employeeRole;
    public InfoRestaurantDto restaurant;
    public EmployeeRole employeeRoleModel;
    public Restaurant employeeRestaurantModel;
    public String employeeRoleName;
    public String restaurantName;
    public String password;
    public double salary;

    public static EmployeeInformationDto of(Employee employee) {
        EmployeeInformationDto dto = new EmployeeInformationDto();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setLastName(employee.getLastName());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setEmail(employee.getEmail());
        dto.setSalary(employee.getSalary());
        dto.setEmployeeRoleName(employee.getEmployeeRole().getName());
        dto.setRestaurantName(employee.getRestaurant().getName());

        dto.setPassword(employee.getPassword());
        dto.setRestaurant(InfoRestaurantDto.of(employee.getRestaurant()));
        return dto;
    }
}
