package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.employee.AddEmployeeDto;
import com.example.restaurantapi.dto.employee.EmployeeInformationDto;
import com.example.restaurantapi.service.EmployeeService;
import com.example.restaurantapi.service.ServiceReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Szymon Królik
 */
@RequestMapping("/employee")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {

    private final EmployeeService employeeService;

    @PutMapping("/add-employee")
    public ServiceReturn addEmployeeToRestaurant(@RequestBody AddEmployeeDto dto) {
        return employeeService.addEmployeeToRestaurant(dto);
    }

    @GetMapping("/get-employees")
    public ServiceReturn getAllEmployeesRestaurant(@RequestParam("restaurantId") int restaurantId) {
        return employeeService.getAllEmployeesRestaurant(restaurantId);
    }
    @GetMapping("/get-employee")
    public ServiceReturn getEmployee(@RequestParam("employeeId") int employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @GetMapping("/update-employee")
    public ServiceReturn updateEmployee(@RequestParam("employeeId") int employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @PutMapping("/update-employee")
    public ServiceReturn updateEmployee(@RequestBody EmployeeInformationDto dto) {
        return employeeService.updateEmployee(dto);
    }

    @GetMapping("/get-roles")
    public ServiceReturn getRoles() {
        return employeeService.getRoles();
    }

    @GetMapping("/get-employee-by-owner")
    public ServiceReturn getEmployeeByOwner(@RequestParam("ownerId") int ownerId) {
        return employeeService.getEmployeeByOwner(ownerId);
    }
}
