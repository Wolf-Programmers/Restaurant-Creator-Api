package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.employee.AddEmployeeDto;
import com.example.restaurantapi.dto.employee.CreatedEmployeeDto;
import com.example.restaurantapi.dto.employee.EmployeeInformationDto;
import com.example.restaurantapi.model.Employee;
import com.example.restaurantapi.model.EmployeeRole;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.UserRole;
import com.example.restaurantapi.repository.EmployeeRepository;
import com.example.restaurantapi.repository.EmployeeRoleRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

        try {
            Employee createdEmployee = employeeRepository.save(employee);
            ret.setValue(CreatedEmployeeDto.of(createdEmployee));
            ret.setStatus(1);
        } catch (Exception ex) {
            ret.setMessage("Err. add employee" + ex.getMessage());
            ret.setStatus(-1);
        }


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

        List<EmployeeInformationDto> employeeInformationDtoList = employeeList.stream()
                .map(x -> EmployeeInformationDto.of(x)).collect(Collectors.toList());

        ret.setValue(employeeInformationDtoList);
        ret.setStatus(1);
        return ret;
    }

    public ServiceReturn getEmployee(int employeeId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isEmpty()) {
            ret.setMessage("Nie znaleziono takiego pracownika");
            ret.setStatus(0);
            return ret;
        }
        EmployeeInformationDto employeeInformationDto = EmployeeInformationDto.of(employeeOptional.get());
        ret.setValue(employeeInformationDto);
        ret.setStatus(1);
        return ret;
    }

    public ServiceReturn updateEmployee(EmployeeInformationDto dto) {
      ServiceReturn ret = new ServiceReturn();
        Optional<Employee> optionalEmployee = employeeRepository.findById(dto.getId());
        if (optionalEmployee.isEmpty()) {
            ret.setMessage("Nie znaleziono takiego użytkownika");
            ret.setStatus(0);
            return ret;
        }
        Optional<EmployeeRole> optionalEmployeeRole = employeeRoleRepository.findById(dto.getEmployeeRoleId());
        if (optionalEmployeeRole.isEmpty()) {
            ret.setMessage("Nie znaleziono takiej roli dla pracownika");
            ret.setStatus(0);
            return ret;
        }
        dto.setEmployeeRoleModel(optionalEmployeeRole.get());
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getEmployeeRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setStatus(0);
            return ret;
        }
        dto.setEmployeeRestaurantModel(optionalRestaurant.get());
        try {
            Employee employee = employeeRepository.save(Employee.updateEmployee(optionalEmployee.get(), dto));
            ret.setValue(EmployeeInformationDto.of(employee));
            ret.setStatus(1);
        } catch (Exception ex) {
            ret.setMessage("Err. update employee " + ex.getMessage());
            ret.setStatus(-1);
        }


        return ret;
    }
}
