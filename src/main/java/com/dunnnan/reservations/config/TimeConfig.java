package com.dunnnan.reservations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;

/**
 * Provides configuration for system-wide clock.
 * <p>
 * This clock is meant to reflect the server's local time zone.
 * If you need a fixed time zone (e.g., for consistency across environments),
 * consider using {@code Clock.system(ZoneId.of("Europe/Warsaw"))}.
 */

@Configuration
public class TimeConfig {

    /**
     * Returns the system clock, based on the OS time zone.
     *
     * @return the system default zone {@link Clock}
     */
    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }

}
