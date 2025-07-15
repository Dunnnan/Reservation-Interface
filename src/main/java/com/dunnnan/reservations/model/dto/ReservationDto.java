package com.dunnnan.reservations.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.OffsetTime;

public class ReservationDto {

    @NotBlank(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Resource id is required")
    private Long resourceId;

    @NotBlank(message = "Reservation date is required")
    private LocalDate date;

    @NotBlank(message = "Start time of reservation is required")
    private OffsetTime from;

    @NotBlank(message = "End time of reservation is required")
    private OffsetTime to;

    public ReservationDto() {
    }

    public @NotBlank(message = "User id is required") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotBlank(message = "User id is required") Long userId) {
        this.userId = userId;
    }

    public @NotBlank(message = "Resource id is required") Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(@NotBlank(message = "Resource id is required") Long resourceId) {
        this.resourceId = resourceId;
    }

    public @NotBlank(message = "Reservation date is required") LocalDate getDate() {
        return date;
    }

    public void setDate(@NotBlank(message = "Reservation date is required") LocalDate date) {
        this.date = date;
    }

    public @NotBlank(message = "Start time of reservation is required") OffsetTime getFrom() {
        return from;
    }

    public void setFrom(@NotBlank(message = "Start time of reservation is required") OffsetTime from) {
        this.from = from;
    }

    public @NotBlank(message = "End time of reservation is required") OffsetTime getTo() {
        return to;
    }

    public void setTo(@NotBlank(message = "End time of reservation is required") OffsetTime to) {
        this.to = to;
    }
}
