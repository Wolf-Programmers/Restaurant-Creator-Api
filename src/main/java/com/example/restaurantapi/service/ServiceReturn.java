package com.example.restaurantapi.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author Szymon Królik
 */
@Data
public class ServiceReturn {
    //TODO create log table
    public Object value;
    public int status;
    public Map<String, String> errorList;
    public String message;


    public static ServiceReturn returnInformation(String message, int status) {
        ServiceReturn ret = new ServiceReturn();
        ret.setMessage(message);
        ret.setStatus(status);

        return ret;
    }

    public static ServiceReturn returnInformation(String message, int status, Object value) {
        ServiceReturn ret = new ServiceReturn();
        ret.setMessage(message);
        ret.setStatus(status);
        ret.setValue(value);

        return ret;
    }

    public static ServiceReturn returnError(String message, int status) {
        ServiceReturn ret = new ServiceReturn();
//        logService.saveErrorLogInDatabase(message, status);
        ret.setMessage(message);
        ret.setStatus(status);

        return ret;
    }

    public static ServiceReturn returnError(String message, int status, Map<String, String> errList) {
        ServiceReturn ret = new ServiceReturn();
        ret.setMessage(message);
        ret.setStatus(status);
        ret.setErrorList(errList);

        return ret;
    }

    public static ServiceReturn returnError(String message, int status, Map<String, String> errList, Object value) {
        ServiceReturn ret = new ServiceReturn();
        ret.setValue(value);
        ret.setMessage(message);
        ret.setStatus(status);
        ret.setErrorList(errList);

        return ret;
    }

    public static ServiceReturn returnError(String message, int status, Object value) {
        ServiceReturn ret = new ServiceReturn();
        ret.setMessage(message);
        ret.setStatus(status);
        ret.setValue(value);

        return ret;
    }

}

