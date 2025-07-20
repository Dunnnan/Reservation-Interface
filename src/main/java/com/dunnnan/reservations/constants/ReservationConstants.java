package com.dunnnan.reservations.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
@ConfigurationProperties(prefix = "reservation")
public class ReservationConstants {

    private short maxFrontReservationDays = 14;

    private LocalTime openingTime = LocalTime.of(
            8, 0, 0, 0
    );

    private LocalTime closingTime = LocalTime.of(
            20, 0, 0, 0
    );


    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public short getMaxFrontReservationDays() {
        return maxFrontReservationDays;
    }

    public void setMaxFrontReservationDays(short maxFrontReservationDays) {
        this.maxFrontReservationDays = maxFrontReservationDays;
    }
}
