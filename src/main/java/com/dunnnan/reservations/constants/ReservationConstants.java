package com.dunnnan.reservations.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Configuration class for reservation settings.
 * <p>
 * Contains all configurable parameters related to the reservation service.
 */
@Configuration
@ConfigurationProperties(prefix = "reservation")
public class ReservationConstants {
    /**
     * Maximum number of days into the future for which user can create a reservation.
     */
    private short maxFrontReservationDays = 14;

    /**
     * Default hour from which resource is available for reservation during the day.
     */
    private LocalTime openingTime = LocalTime.of(
            8, 0, 0, 0
    );

    /**
     * Default hour from which resource is unavailable for reservation during the day.
     */
    private LocalTime closingTime = LocalTime.of(
            20, 0, 0, 0
    );

    /**
     * Default duration of a single reservation unit for which resource can be reserved.
     */
    private Duration reservationInterval = Duration.ofMinutes(30);


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

    public Duration getReservationInterval() {
        return reservationInterval;
    }

    public void Duration(Duration reservationInterval) {
        this.reservationInterval = reservationInterval;
    }
}
