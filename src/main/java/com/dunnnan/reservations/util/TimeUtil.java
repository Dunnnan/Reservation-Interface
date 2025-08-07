package com.dunnnan.reservations.util;

import com.dunnnan.reservations.constants.ReservationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeUtil {

    @Autowired
    ReservationConstants reservationConstants;

    public List<LocalTime> getAllPossibleReservationHours(LocalTime start, LocalTime end) {
        List<LocalTime> allPossibleReservationHours = new ArrayList<>();

        LocalTime period = start;
        Duration interval = reservationConstants.getReservationInterval();

        while (period.isBefore(end)) {
            allPossibleReservationHours.add(period);
            period = period.plus(interval);
        }

        // Add last period
        allPossibleReservationHours.add(period);

        return allPossibleReservationHours;
    }

}
