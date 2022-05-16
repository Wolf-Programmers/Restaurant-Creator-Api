package com.example.restaurantapi.dto.employee;

import com.example.restaurantapi.model.EmployeeRole;
import lombok.Data;

import javax.persistence.Column;

@Data
public class EmployeeRoleInformationDto {

    private int id;
    private String name;

    public static EmployeeRoleInformationDto of(EmployeeRole role) {
        EmployeeRoleInformationDto dto = new EmployeeRoleInformationDto();
        dto.setName(role.getName());
        dto.setId(role.getId());

        return dto;
    }
}
