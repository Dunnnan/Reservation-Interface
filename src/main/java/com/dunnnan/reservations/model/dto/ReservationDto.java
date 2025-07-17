package com.dunnnan.reservations.model.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.OffsetTime;

public class ReservationDto {

    @NotNull(message = "Resource id is required")
    private Long resourceId;

    @NotNull(message = "Reservation date is required")
    private LocalDate date;

    @NotNull(message = "Start time of reservation is required")
    private OffsetTime from;

    @NotNull(message = "End time of reservation is required")
    private OffsetTime to;

    public ReservationDto() {
    }

    public @NotNull(message = "Resource id is required") Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(@NotNull(message = "Resource id is required") Long resourceId) {
        this.resourceId = resourceId;
    }

    public @NotNull(message = "Reservation date is required") LocalDate getDate() {
        return date;
    }

    public void setDate(@NotNull(message = "Reservation date is required") LocalDate date) {
        this.date = date;
    }

    public @NotNull(message = "Start time of reservation is required") OffsetTime getFrom() {
        return from;
    }

    public void setFrom(@NotNull(message = "Start time of reservation is required") OffsetTime from) {
        this.from = from;
    }

    public @NotNull(message = "End time of reservation is required") OffsetTime getTo() {
        return to;
    }

    public void setTo(@NotNull(message = "End time of reservation is required") OffsetTime to) {
        this.to = to;
    }
}
