package com.example.restaurantapi.service;


import com.example.restaurantapi.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceFunction {

    private static final String EMAILREGEX = "^(.+)@(.+)$";
    private static final String PHONENUMBERREGEX = "(0|91)?[7-9][0-9]{9}";

    public static boolean isNull(String text) {
        String val = text.trim();
        if (val.equals("") || val.isEmpty() || val.length() == 1) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Object value) {
        if (value.equals(null)) {
            return true;
        }

        return false;
    }

    public static boolean validEmail(String email) {
        Pattern pattern = Pattern.compile(EMAILREGEX,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    public static boolean validPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONENUMBERREGEX);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.find();
    }

    public static boolean enableUser(User user) {
        return user.isEnabled();
    }

}
