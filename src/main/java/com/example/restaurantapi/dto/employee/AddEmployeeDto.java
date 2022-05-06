package com.example.restaurantapi.dto.employee;

import com.example.restaurantapi.model.EmployeeRole;
import lombok.Data;

/**
 * @Author Szymon Królik
 */
@Data
public class AddEmployeeDto {
    public String name;
    public String lastName;
    public String phoneNumber;
    public String email;
    public String password;
    public Double salary;
    public int restaurantId;
    public int employeeRoleId;

}

