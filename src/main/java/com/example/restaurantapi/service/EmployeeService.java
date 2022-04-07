package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.restaurant.AddEmployeeDto;
import com.example.restaurantapi.dto.restaurant.CreatedEmployeeDto;
import com.example.restaurantapi.model.Employee;
import com.example.restaurantapi.model.EmployeeRole;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.repository.EmployeeRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author Szymon Królik
 */
@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantRepository restaurantRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationService validationService;
    private List<String> validationResult = new ArrayList<>();

    public ServiceReturn addEmployeeToRestaurant(AddEmployeeDto dto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();
        validationResult = validationService.addEmployeeValidation(dto);
        if (validationResult.size() > 0) {
            ret.setMessage("Wystąpiły błedy");
            ret.setValue(dto);
            ret.setErrorList(validationResult);
            return ret;
        }

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setValue(dto);
            return ret;
        }

        Employee employee = Employee.of(dto);
        employee.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        employee.setRole(EmployeeRole.EMPLOYEE);
        employee.setRestaurant(optionalRestaurant.get());


        Employee createdEmployee = employeeRepository.save(employee);
        ret.setValue(CreatedEmployeeDto.of(createdEmployee));

        return ret;
    }
}
