package com.example.restaurantapi.biznesobject;

import com.example.restaurantapi.model.OpeningPeriod;
import lombok.Data;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class OpeningTimes {
    private int dayOfWeek;
    private String from;
    private String to;

    public static OpeningTimes of (OpeningPeriod openingPeriod) {
        OpeningTimes dto = new OpeningTimes();

        dto.setDayOfWeek(openingPeriod.getWeekDay());
        dto.setFrom(openingPeriod.getOpeningTime().toString());
        dto.setTo(openingPeriod.getClosingTime().toString());

        return dto;
    }
}
