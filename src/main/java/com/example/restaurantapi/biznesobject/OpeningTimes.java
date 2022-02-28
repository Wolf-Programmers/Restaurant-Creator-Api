package com.example.restaurantapi.biznesobject;

import lombok.Data;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class OpeningTimes {
    private int dayOfWeek;
    private String from;
    private String to;
}
