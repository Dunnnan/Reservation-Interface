package com.dunnnan.reservations.service;

import com.dunnnan.reservations.model.Reservation;
import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.OffsetTime;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    public boolean isReservationPeriodFree(
            LocalDate date, Long id, OffsetTime from, OffsetTime to) {
        return reservationRepository.findByDateAndResource_IdAndFromLessThanAndToGreaterThan(date, id, from, to).isEmpty();
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

    public BindingResult reserve(ReservationDto reservationDto, BindingResult result) {
        if (resourceService.getResourceById(reservationDto.getResourceId()).isEmpty()) {
            result.rejectValue("date", "error.date", "Resource doesn't exist!");
            return result;
        }

        if (!isReservationPeriodFree(
                reservationDto.getDate(),
                reservationDto.getResourceId(),
                reservationDto.getFrom(),
                reservationDto.getTo()
        )) {
            result.rejectValue("from", "error.from", "Reservation period is already reserved!");
        }

        registerReservation(reservationDto);
        return result;
    }

}
