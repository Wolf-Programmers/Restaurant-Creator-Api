package com.example.restaurantapi.service;


import com.example.restaurantapi.model.User;


import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Szymon Królik
 */
public class ServiceFunction {

    private static final String EMAILREGEX = "^(.+)@(.+)$";
    private static final String PHONENUMBERREGEX = "(0|91)?[7-9][0-9]{9}";

    public static boolean isNull(String text) {
        if (text == null)
            return true;
        return text.equals("") || text.isEmpty() || text.length() == 1;
    }

    public static boolean isNull(Object value) {
        return value == null;
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

    public static Time getTimeFromString(String time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm");

        return new Time(formatter.parse(time).getTime());
    }

    public static void clear(List<String> var) {
        var.clear();
    }

    public static boolean enableUser(User user) {
        return user.isEnabled();
    }

}
