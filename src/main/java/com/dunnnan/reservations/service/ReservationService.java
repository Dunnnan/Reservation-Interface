package com.dunnnan.reservations.service;

import com.dunnnan.reservations.constants.ReservationConstants;
import com.dunnnan.reservations.model.Reservation;
import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.repository.ReservationRepository;
import com.dunnnan.reservations.util.TimeUtil;
import com.dunnnan.reservations.validation.ReservationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ReservationService {

    private final Clock clock;

    @Autowired
    ReservationValidator reservationValidator;

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
        result = reservationValidator.validateReservation(reservationDto, result);

        if (result.hasErrors()) {
            return result;
        }

        registerReservation(reservationDto);
        return result;
    }

    public List<Reservation> getAllReservationsByDateAndResourceId(Long id, LocalDate date) {
        return reservationRepository.findByDateAndResource_Id(date, id);
    }

    public Set<LocalTime> filterHoursByNeighbor(Set<LocalTime> hoursSet, Duration interval, boolean requireBothNeighbors) {
        return hoursSet.stream()
                .filter(hour -> {
                    boolean hasPrev = hoursSet.contains(hour.minus(interval));
                    boolean hasNext = hoursSet.contains(hour.plus(interval));
                    return requireBothNeighbors ? (hasPrev && hasNext) : (hasPrev || hasNext);
                })
                .collect(Collectors.toSet());
    }

    public List<String> getValidReservationHoursForDay(Long resourceId, LocalDate date) {
        Duration interval = reservationConstants.getReservationInterval();

        // Get availability period of resource
        List<LocalTime> availability = availabilityService.getAvailabilityTimePeriod(resourceId, date);
        LocalTime openingTime = availability.get(0);
        LocalTime closingTime = availability.get(1);

        // Specify occupied hours
        Set<LocalTime> occupiedHours = getAllReservationsByDateAndResourceId(resourceId, date).stream()
                .flatMap(reservation -> timeUtil.getAllPossibleReservationHours(
                        reservation.getFrom(), reservation.getTo()).stream()
                )
                .collect(Collectors.toSet());

        // Remove hours that are not part of the reservation (beginning / end)
        occupiedHours = filterHoursByNeighbor(occupiedHours, interval, true);

        // Get all hours possible hours and exclude occupied ones
        Set<LocalTime> validHours = new HashSet<>(timeUtil.getAllPossibleReservationHours(
                openingTime, closingTime));
        validHours.removeAll(occupiedHours);

        // Remove past hours (today)
        if (LocalDate.now(clock).equals(date)) {
            validHours = validHours.stream()
                    .filter(hour -> hour.isAfter(LocalTime.now(clock)))
                    .collect(Collectors.toSet());
        }

        // Remove isolated hours (have no neighbor)
        validHours = filterHoursByNeighbor(validHours, interval, false);

        return validHours.stream()
                .sorted()
                .map(LocalTime::toString)
                .toList();
    }

    public Map<Integer, List<String>> getValidReservationHoursForWeek(Long resourceId, LocalDate date) {
        Map<Integer, List<String>> availableHoursForWeek = new HashMap<>();

        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        return IntStream.range(0, 7)
                .mapToObj(monday::plusDays)
                .collect(Collectors.toMap(
                        day -> day.getDayOfWeek().getValue(),
                        day -> getValidReservationHoursForDay(resourceId, day),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }

}
