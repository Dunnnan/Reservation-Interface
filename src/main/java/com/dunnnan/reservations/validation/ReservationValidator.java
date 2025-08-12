package com.dunnnan.reservations.validation;

import com.dunnnan.reservations.constants.ReservationConstants;
import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.repository.ReservationRepository;
import com.dunnnan.reservations.service.AvailabilityService;
import com.dunnnan.reservations.service.ResourceService;
import com.dunnnan.reservations.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ReservationValidator {

    private final Clock clock;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private ReservationConstants reservationConstants;

    @Autowired
    private TimeUtil timeUtil;

    public ReservationValidator(Clock clock) {
        this.clock = clock;
    }

    public boolean isReservationPeriodFree(
            LocalDate date, Long id, LocalTime from, LocalTime to) {
        return reservationRepository.findByDateAndResource_IdAndFromLessThanAndToGreaterThan(date, id, to, from).isEmpty();
    }

    public boolean timePeriodIsNotInThePast(LocalTime to, LocalTime from, LocalDate date) {
        return LocalDate.now(clock).isBefore(date) || LocalTime.now(clock).isBefore(from);
    }

    public boolean timePeriodIsCorrectAndNotNull(LocalTime to, LocalTime from) {
        return from.isBefore(to);
    }

    public boolean resourceExists(Long id) {
        return resourceService.getResourceById(id).isPresent();
    }

    public Integer validateWeeksLater(Integer weeksLater) {
        if (weeksLater < 0) {
            return 0;
        } else if (weeksLater > reservationConstants.getMaxFrontReservationDays() / 7) {
            return (int) reservationConstants.getMaxFrontReservationDays() / 7;
        }

        return weeksLater;
    }

    public BindingResult validateReservation(ReservationDto reservationDto, BindingResult result) {
        if (!timePeriodIsNotInThePast(reservationDto.getTo(), reservationDto.getFrom(), reservationDto.getDate())) {
            result.rejectValue("from", "error.from", "Resource reservation period is in the past!");
            return result;
        }

        if (!timePeriodIsCorrectAndNotNull(reservationDto.getTo(), reservationDto.getFrom())) {
            result.rejectValue("from", "error.from", "Resource reservation period is invalid!");
            return result;
        }

        if (!resourceExists(reservationDto.getResourceId())) {
            result.rejectValue("date", "error.date", "Resource doesn't exist!");
            return result;
        }

        if (!availabilityService.isAvailable(reservationDto.getDate(), reservationDto.getResourceId())) {
            result.rejectValue("date", "error.date", "Resource isn't available in that day!");
            return result;
        }

        if (!isReservationPeriodFree(
                reservationDto.getDate(),
                reservationDto.getResourceId(),
                reservationDto.getFrom(),
                reservationDto.getTo()
        )) {
            result.rejectValue("from", "error.from", "Reservation period is already reserved!");
            return result;
        }

        return result;
    }

}
