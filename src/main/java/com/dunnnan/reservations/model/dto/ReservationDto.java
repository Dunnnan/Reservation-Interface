package com.dunnnan.reservations.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public class ReservationDto {

    @NotBlank(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Resource id is required")
    private Long resourceId;

    @NotBlank(message = "Start time of reservation is required")
    private OffsetDateTime from;

    @NotBlank(message = "End time of reservation is required")
    private OffsetDateTime to;

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

    public @NotBlank(message = "Start time of reservation is required") OffsetDateTime getFrom() {
        return from;
    }

    public void setFrom(@NotBlank(message = "Start time of reservation is required") OffsetDateTime from) {
        this.from = from;
    }

    public @NotBlank(message = "End time of reservation is required") OffsetDateTime getTo() {
        return to;
    }

    public void setTo(@NotBlank(message = "End time of reservation is required") OffsetDateTime to) {
        this.to = to;
    }
}
