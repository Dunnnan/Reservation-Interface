package com.dunnnan.reservations;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@TestConfiguration
public class TestConfig {

    @Bean("fixedClock")
    @Primary
    public Clock fixedClock() {
        return Clock.fixed(
                LocalTime.of(10, 0).atDate(LocalDate.now())
                        .atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
    }

}
