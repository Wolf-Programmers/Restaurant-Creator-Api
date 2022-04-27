package com.example.restaurantapi.service;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author Szymon Królik
 */
@Data
public class ServiceReturn {
    public Object value;
    public int status;
    public Map<String, String> errorList;
    public String message;
}
