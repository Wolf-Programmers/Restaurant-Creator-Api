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
        if (employeeRepository.findEmployeeByEmail(dto.getEmail()).isPresent() || employeeRepository.findEmployeeByPhoneNumber(dto.getPhoneNumber()).isPresent())
            return ServiceReturn.returnError("Employee with given email or phone number already exist", 0, dto);

        if (validationResult.size() > 0)
            return ServiceReturn.returnError("Validation error", -1, validationResult, dto);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getRestaurantId());
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0,dto);

        Optional<EmployeeRole> optionalEmployeeRole = employeeRoleRepository.findById(dto.getEmployeeRoleId());
        if (!optionalEmployeeRole.isPresent())
            return ServiceReturn.returnError("Can't find employee role with given id", 0,dto);

        Employee employee = Employee.of(dto);
        employee.setEmployeeRole(optionalEmployeeRole.get());
        employee.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        employee.setRestaurant(optionalRestaurant.get());

        try {
            Employee createdEmployee = employeeRepository.save(employee);
            return ServiceReturn.returnInformation("Employee saved", 1,CreatedEmployeeDto.of(createdEmployee));
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. add employee" + ex.getMessage(), -1);
        }

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
        if (employeeList.isEmpty())
            return ServiceReturn.returnError("Can't find employees", 0);


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
        if (!employeeOptional.isPresent())
            return ServiceReturn.returnError("Can't find employee with given id", 0, employeeId);

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
        if (!optionalEmployee.isPresent())
            return ServiceReturn.returnError("Can't find employee with given id", 0, dto.getId());

        Optional<EmployeeRole> optionalEmployeeRole = employeeRoleRepository.findById(dto.getEmployeeRoleId());
        if (!optionalEmployeeRole.isPresent())
            return ServiceReturn.returnError("Can't find employee role with given id", 0, dto.getEmployeeRoleId());

        dto.setEmployeeRoleModel(optionalEmployeeRole.get());
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getEmployeeRestaurantId());
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, dto.getEmployeeRestaurantId());

        dto.setEmployeeRestaurantModel(optionalRestaurant.get());
        try {
            Employee employee = employeeRepository.save(Employee.updateEmployee(optionalEmployee.get(), dto));
            return ServiceReturn.returnInformation("Update employee success", 1, EmployeeInformationDto.of(employee));
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. update employee" + ex.getMessage(), -1);
        }
    }

    /**
     * get all roles for emploee
     *@author Szymon Królik
     * @return List<EmployeeRole>
     */
    public ServiceReturn getRoles() {
        ServiceReturn ret = new ServiceReturn();
        List<EmployeeRole> employeeRoles = employeeRoleRepository.findAll();
        if (employeeRoles.isEmpty())
            return ServiceReturn.returnError("Can't find employee roles", -1, employeeRoles);
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
        if (!userOptional.isPresent())
            return ServiceReturn.returnError("Can't find user with given id", 0, ownerId);


        List<Restaurant> restaurantList = userOptional.get().getRestaurants();

        if (restaurantList.size() > 0) {
            List<Employee> employeeList = restaurantList.stream().map(x -> x.getEmployees()).collect(Collectors.toList())
                    .stream().flatMap(List::stream).collect(Collectors.toList());
            List<EmployeeInformationDto> employeeInformationDtoList = employeeList.stream().map(x -> EmployeeInformationDto.of(x)).collect(Collectors.toList());
            ret.setValue(employeeInformationDtoList);
            ret.setStatus(1);
            return ret;
        } else {
            return ServiceReturn.returnError("Can't find any restaurant for user", 0, ownerId);
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
                return ServiceReturn.returnError("Err. delete employee" + ex.getMessage(), -1, deleteEmployee);
            }
        } else {
            return ServiceReturn.returnError("Can't find employee with given id", 0, deleteEmployee);
        }
        return ret;
    }
}
