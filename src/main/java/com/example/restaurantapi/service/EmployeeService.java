package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.employee.AddEmployeeDto;
import com.example.restaurantapi.dto.employee.CreatedEmployeeDto;
import com.example.restaurantapi.dto.employee.EmployeeInformationDto;
import com.example.restaurantapi.model.*;
import com.example.restaurantapi.repository.EmployeeRepository;
import com.example.restaurantapi.repository.EmployeeRoleRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import com.example.restaurantapi.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationService validationService;
    private Map<String, String> validationResult = new HashMap<String, String>();

    /**
     * @author Szymon Królik
     * @param dto
     * @return CreatedEmployeeDto
     */
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
        if (!optionalRestaurant.isPresent()) {
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setValue(dto);
            return ret;
        }

        Optional<EmployeeRole> optionalEmployeeRole = employeeRoleRepository.findById(dto.getEmployeeRoleId());
        if (!optionalEmployeeRole.isPresent()) {
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

    /**
     * get all employees for specific restaurant
      *@author Szymon Królik
     * @param restaurantId
     * @return List<EmployeeInformationDto>
     */
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

    /**
     * get information about specific employee
     *@author Szymon Królik
      * @param employeeId
     * @return EmployeeInformationDto
     */
    public ServiceReturn getEmployee(int employeeId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (!employeeOptional.isPresent()) {
            ret.setMessage("Nie znaleziono takiego pracownika");
            ret.setStatus(0);
            return ret;
        }
        EmployeeInformationDto employeeInformationDto = EmployeeInformationDto.of(employeeOptional.get());
        ret.setValue(employeeInformationDto);
        ret.setStatus(1);
        return ret;
    }

    /**
     * update employee
       * @author Szymon Królik
     * @return EmployeeInformationDto
     */
    public ServiceReturn updateEmployee(EmployeeInformationDto dto) {
      ServiceReturn ret = new ServiceReturn();
        Optional<Employee> optionalEmployee = employeeRepository.findById(dto.getId());
        if (!optionalEmployee.isPresent()) {
            ret.setMessage("Nie znaleziono takiego użytkownika");
            ret.setStatus(0);
            return ret;
        }
        Optional<EmployeeRole> optionalEmployeeRole = employeeRoleRepository.findById(dto.getEmployeeRoleId());
        if (!optionalEmployeeRole.isPresent()) {
            ret.setMessage("Nie znaleziono takiej roli dla pracownika");
            ret.setStatus(0);
            return ret;
        }
        dto.setEmployeeRoleModel(optionalEmployeeRole.get());
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getEmployeeRestaurantId());
        if (!optionalRestaurant.isPresent()) {
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

    /**
     * get all roles for emploee
     *@author Szymon Królik
     * @return List<EmployeeRole>
     */
    public ServiceReturn getRoles() {
        ServiceReturn ret = new ServiceReturn();
        List<EmployeeRole> employeeRoles = employeeRoleRepository.findAll();
        ret.setValue(employeeRoles);
        ret.setStatus(1);

        return ret;

    }

    /**
     * get all employees for owner
     *@author Szymon Królik
      * @param ownerId
      * @return List<EmployeeInformationDto>
     */
    public ServiceReturn getEmployeeByOwner(int ownerId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<User> userOptional = userRepository.findById(ownerId);
        if (!userOptional.isPresent()) {
            ret.setMessage("Nie znaleziono takiego użytkownika");
            ret.setStatus(0);
            return ret;
        }

        List<Restaurant> restaurantList = userOptional.get().getRestaurants();

        if (restaurantList.size() > 0) {
            List<Employee> employeeList = restaurantList.stream().map(x -> x.getEmployees()).collect(Collectors.toList())
                    .stream().flatMap(List::stream).collect(Collectors.toList());
            List<EmployeeInformationDto> employeeInformationDtoList = employeeList.stream().map(x -> EmployeeInformationDto.of(x)).collect(Collectors.toList());
            ret.setValue(employeeInformationDtoList);
            ret.setStatus(1);
            return ret;
        } else {
            ret.setMessage("Uzytkownik nie posiada żadnych resturacji");
            ret.setStatus(0);
            return ret;
        }
    }

    /**
     * delete employee by id
     *@author Szymon Królik
     * @param deleteEmployee
     */
    public ServiceReturn deleteEmployee(int deleteEmployee) {
        ServiceReturn ret = new ServiceReturn();

        Optional<Employee> optionalEmployee = employeeRepository.findById(deleteEmployee);
        if (optionalEmployee.isPresent()) {
            try {
                    employeeRepository.delete(optionalEmployee.get());
                    ret.setStatus(1);
            } catch (Exception ex) {
                ret.setMessage(ex.getMessage());
                ret.setStatus(-1);
                return  ret;
            }
        } else {
            ret.setMessage("Nie znaleziono takiego pracownika");
            ret.setStatus(0);
            return ret;
        }
       return ret;
    }
}
