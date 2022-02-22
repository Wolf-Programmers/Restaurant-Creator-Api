package com.example.restaurantapi.service;

import lombok.Data;

import java.util.List;

@Data
public class ServiceReturn {
    public Object value;
    public int status;
    public List<String> errorList;
    public String message;
}
