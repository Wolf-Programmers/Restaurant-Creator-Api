package com.example.restaurantapi.model;

import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.service.ServiceFunction;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.text.ParseException;

@Data
@Entity
@Table(name = "openingPeriod")
public class OpeningPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private int weekDay;

    @Column(nullable = false)
    private Time openingTime;

    @Column(nullable = false)
    private Time closingTime;

    //Restaurant id
    @OneToOne(targetEntity = Restaurant.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "restaurant_id")
    private Restaurant restaurant;

    public static OpeningPeriod of(OpeningTimes openingTimes) throws ParseException {
        OpeningPeriod period = new OpeningPeriod();
        period.setWeekDay(openingTimes.getDayOfWeek());
        period.setOpeningTime(ServiceFunction.getTimeFromString(openingTimes.getFrom()));
        period.setClosingTime(ServiceFunction.getTimeFromString(openingTimes.getTo()));

        return period;
    }


}
