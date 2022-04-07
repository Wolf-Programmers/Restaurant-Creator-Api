package com.example.restaurantapi.model;

import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.service.ServiceFunction;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Time;
import java.text.ParseException;

@Data
@Entity
@Table(name = "openingPeriod")
public class OpeningPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int weekDay;

    @Column(nullable = false)
    private Time openingTime;

    @Column(nullable = false)
    private Time closingTime;

    //Restaurant id
//    @OneToOne(targetEntity = Restaurant.class, fetch = FetchType.EAGER)
//    @JoinColumn(nullable = false, name = "restaurant_id")
//    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Restaurant restaurant;

    public static OpeningPeriod of(OpeningTimes openingTimes) throws ParseException {
        OpeningPeriod period = new OpeningPeriod();
        period.setWeekDay(openingTimes.getDayOfWeek());
        period.setOpeningTime(ServiceFunction.getTimeFromString(openingTimes.getFrom()));
        period.setClosingTime(ServiceFunction.getTimeFromString(openingTimes.getTo()));

        return period;
    }

    public static OpeningPeriod ofOpeningPeriod(OpeningPeriod dto) {
        OpeningPeriod ret = new OpeningPeriod();
        ret.setOpeningTime(dto.getOpeningTime());
        ret.setClosingTime(dto.getClosingTime());
        ret.setWeekDay(dto.getWeekDay());

        return ret;
    }


}
