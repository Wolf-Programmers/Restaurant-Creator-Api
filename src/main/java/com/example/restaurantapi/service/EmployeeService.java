package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.employee.AddEmployeeDto;
import com.example.restaurantapi.dto.employee.CreatedEmployeeDto;
import com.example.restaurantapi.dto.employee.EmployeeInformationDto;
import com.example.restaurantapi.model.Employee;
import com.example.restaurantapi.model.EmployeeRole;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.repository.EmployeeRepository;
import com.example.restaurantapi.repository.EmployeeRoleRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author Szymon Królik
 */
@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantRepository restaurantRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationService validationService;
    private Map<String, String> validationResult = new HashMap<String, String>();

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

        Optional<EmployeeRole> optionalEmployeeRole = employeeRoleRepository.findById(dto.getEmployeeRoleId());
        if (optionalEmployeeRole.isEmpty()) {
            ret.setMessage("Nie znaleziono takiej roli użytkownika w bazie danych");
            ret.setValue(dto);
            return ret;
        }

        Employee employee = Employee.of(dto);
        employee.setEmployeeRole(optionalEmployeeRole.get());
        employee.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        employee.setRestaurant(optionalRestaurant.get());


        Employee createdEmployee = employeeRepository.save(employee);
        ret.setValue(CreatedEmployeeDto.of(createdEmployee));

        return ret;
    }

    public ServiceReturn getAllEmployeesRestaurant(int restaurantId) {
        ServiceReturn ret = new ServiceReturn();

        List<Employee> employeeList = employeeRepository.findByRestaurantId(restaurantId);
        if (employeeList.isEmpty()) {
            ret.setMessage("Brak pracowników");
            ret.setStatus(0);
            return ret;
        }

        List<EmployeeInformationDto> employeeInformationDtoList = new ArrayList<>();
        for (Employee employee : employeeList) {
            EmployeeInformationDto dto = EmployeeInformationDto.of(employee);
            employeeInformationDtoList.add(dto);
        }

        ret.setValue(employeeInformationDtoList);
        ret.setStatus(1);
        return ret;



    }
}
