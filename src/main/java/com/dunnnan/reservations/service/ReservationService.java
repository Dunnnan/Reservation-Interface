package com.dunnnan.reservations.service;

import com.dunnnan.reservations.constants.ReservationConstants;
import com.dunnnan.reservations.model.Reservation;
import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.repository.ReservationRepository;
import com.dunnnan.reservations.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    private final Clock clock;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private ReservationConstants reservationConstants;

    @Autowired
    private TimeUtil timeUtil;

    public ReservationService(Clock clock) {
        this.clock = clock;
    }

    public List<Reservation> getAllReservationsByDateAndResourceId(Long id, LocalDate date) {
        return reservationRepository.findByDateAndResource_Id(date, id);
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

        registerReservation(reservationDto);
        return result;
    }

    public List<LocalTime> getValidReservationHours(Long resourceId, LocalDate date) {
        // Availability period of resource
        List<LocalTime> availability = availabilityService.getAvailabilityTimePeriod(resourceId, date);
        LocalTime openingTime = availability.get(0);
        LocalTime closingTime = availability.get(1);

        // All reservations of resource
        List<Reservation> reservations = getAllReservationsByDateAndResourceId(resourceId, date);
        Set<LocalTime> occupiedHours = new HashSet<>();

        // Specifying occupied hours
        for (Reservation reservation : reservations) {
            List<LocalTime> reservationHours = timeUtil.getAllPossibleReservationHours(
                    reservation.getFrom(), reservation.getTo());
            occupiedHours.addAll(reservationHours);
        }

        // Filtering occupied hours
        Set<LocalTime> validHours = new HashSet<>(timeUtil.getAllPossibleReservationHours(
                openingTime, closingTime));

        validHours.removeAll(occupiedHours);

        return validHours.stream()
                .sorted()
                .toList();
    }

}
