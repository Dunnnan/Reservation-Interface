package com.dunnnan.reservations.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetTime;
import java.time.ZoneOffset;

@Configuration
@ConfigurationProperties(prefix = "reservation")
public class ReservationConfig {

    private short maxFrontReservationDays = 14;

    private OffsetTime openingTime = OffsetTime.of(
            8, 0, 0, 0, ZoneOffset.ofHours(1)
    );
    private OffsetTime closingTime = OffsetTime.of(
            20, 0, 0, 0, ZoneOffset.ofHours(1)
    );


    public OffsetTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(OffsetTime openingTime) {
        this.openingTime = openingTime;
    }

    public OffsetTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(OffsetTime closingTime) {
        this.closingTime = closingTime;
    }

    public short getMaxFrontReservationDays() {
        return maxFrontReservationDays;
    }

    public void setMaxFrontReservationDays(short maxFrontReservationDays) {
        this.maxFrontReservationDays = maxFrontReservationDays;
    }
}
