package com.dunnnan.reservations.service;

import com.dunnnan.reservations.model.Reservation;
import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AvailabilityService availabilityService;

    public boolean isReservationPeriodFree(
            LocalDate date, Long id, LocalTime from, LocalTime to) {
        return reservationRepository.findByDateAndResource_IdAndFromLessThanAndToGreaterThan(date, id, to, from).isEmpty();
    }

    public void registerReservation(ReservationDto reservationDto) {
        Reservation reservation = new Reservation(
                userService.findUserId(userService.getUserId()),
                resourceService.findResourceById(reservationDto.getResourceId()),
                reservationDto.getDate(),
                reservationDto.getFrom(),
                reservationDto.getTo()
        );
        reservationRepository.save(reservation);
    }

    public boolean timePeriodIsCorrect(LocalTime to, LocalTime from) {
        return to.isBefore(from);
    }

    public boolean resourceExists(Long id) {
        return resourceService.getResourceById(id).isPresent();
    }

    public BindingResult reserve(ReservationDto reservationDto, BindingResult result) {
        if (timePeriodIsCorrect(reservationDto.getTo(), reservationDto.getFrom())) {
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

        registerReservation(reservationDto);
        return result;
    }

}
