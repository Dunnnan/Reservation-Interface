package com.dunnnan.reservations.service;

import com.dunnnan.reservations.constants.ReservationConstants;
import com.dunnnan.reservations.model.Reservation;
import com.dunnnan.reservations.model.dto.ReservationDto;
import com.dunnnan.reservations.repository.ReservationRepository;
import com.dunnnan.reservations.util.TimeUtil;
import com.dunnnan.reservations.validation.ReservationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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

    public Set<LocalTime> filterHoursByNeighbor(Set<LocalTime> filterSet, Set<LocalTime> referenceSet, Duration offset) {
        Set<LocalTime> originalReference = new HashSet<>(referenceSet);

        return filterSet.stream()
                .filter(hour -> originalReference.contains(hour.plus(offset)))
                .collect(Collectors.toSet());
    }

    public Set<LocalTime> getOccupiedHoursDuringTheDay(Long resourceId, LocalDate date, String hoursType) {
        if (hoursType.equalsIgnoreCase("from")) {
            return getAllReservationsByDateAndResourceId(resourceId, date).stream()
                    .flatMap(reservation -> timeUtil.getAllPossibleReservationHours(
                            reservation.getFrom(), reservation.getTo(), false).stream()
                    )
                    .collect(Collectors.toSet());
        } else if (hoursType.equalsIgnoreCase("to")) {
            return getAllReservationsByDateAndResourceId(resourceId, date).stream()
                    .flatMap(reservation -> timeUtil.getAllPossibleReservationHours(
                            reservation.getFrom().plus(reservationConstants.getReservationInterval()), reservation.getTo(), true).stream()
                    )
                    .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

    public Set<LocalTime> getAvailableHours(LocalDate date, LocalTime openingTime, LocalTime closingTime, Set<LocalTime> occupiedHours, boolean includeLastPeriod) {
        Set<LocalTime> hours = new HashSet<>(timeUtil.getAllPossibleReservationHours(
                openingTime, closingTime, includeLastPeriod
        ));
        hours.removeAll(occupiedHours);

        // Remove past hours (today)
        if (LocalDate.now(clock).equals(date)) {
            hours = hours.stream()
                    .filter(hour -> hour.isAfter(LocalTime.now(clock)))
                    .collect(Collectors.toSet());
        }

        return hours;
    }

    public List<LocalTime> getMaxReservationHourRangeForWeek(Long resourceId, Integer weeksLater) {
        LocalDate monday = LocalDate.now(clock)
                .plusWeeks(weeksLater)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<LocalDate> calendarDays = IntStream.range(0, 7)
                .mapToObj(monday::plusDays)
                .toList();

        List<LocalTime> calendarHours = new ArrayList<>();

        for (LocalDate day : calendarDays) {
            List<LocalTime> availability = availabilityService.getAvailabilityTimePeriodOrReturnEmptyList(resourceId, day);
            if (!availability.isEmpty()) {
                calendarHours.add(availability.get(0));
                calendarHours.add(availability.get(1));
            }
        }

        // Return empty list if there is none available time periods
        if (calendarHours.isEmpty()) {
            return Collections.emptyList();
        }

        return timeUtil.getAllPossibleReservationHours(
                Collections.min(calendarHours),
                Collections.max(calendarHours),
                false
        );
    }

    public List<List<String>> getValidReservationHoursForDay(Long resourceId, LocalDate date) {
        Duration interval = reservationConstants.getReservationInterval();

        // Get availability period of resource
        List<LocalTime> availability = availabilityService.getAvailabilityTimePeriod(resourceId, date);
        LocalTime openingTime = availability.get(0);
        LocalTime closingTime = availability.get(1);

        // Specify occupied hours
        Set<LocalTime> occupiedFromHours = getOccupiedHoursDuringTheDay(resourceId, date, "from");
        Set<LocalTime> occupiedToHours = getOccupiedHoursDuringTheDay(resourceId, date, "to");

        // Get all available hours
        Set<LocalTime> fromHours = getAvailableHours(date, openingTime, closingTime, occupiedFromHours, false);
        Set<LocalTime> toHours = getAvailableHours(date, openingTime.plus(interval), closingTime, occupiedToHours, true);

        // Remove isolated hours (have no neighbor)
        fromHours = filterHoursByNeighbor(fromHours, toHours, interval);
        toHours = filterHoursByNeighbor(toHours, fromHours, interval.negated());

        return List.of(
                fromHours.stream()
                        .sorted()
                        .map(LocalTime::toString)
                        .toList(),
                toHours.stream()
                        .sorted()
                        .map(LocalTime::toString)
                        .toList()
        );
    }

    public List<List<String>> getAllPossibleReservationHoursWithStatus(Long resourceId, LocalDate date) {
        Duration interval = reservationConstants.getReservationInterval();

        // Get availability period of resource
        List<LocalTime> availability = availabilityService.getAvailabilityTimePeriodOrReturnEmptyList(resourceId, date);
        if (availability.isEmpty()) {
            return List.of(List.of("0:00"), List.of("Null"));
        }

        LocalTime openingTime = availability.get(0);
        LocalTime closingTime = availability.get(1);

        // Get possible reservation hours
        List<LocalTime> allPossibleHours = timeUtil.getAllPossibleReservationHours(openingTime, closingTime, false);

        // Specify occupied hours
        Set<LocalTime> occupiedHours = getOccupiedHoursDuringTheDay(resourceId, date, "from");

        List<String> hourLabels = new ArrayList<>();
        List<String> hourStatuses = new ArrayList<>();

        for (LocalTime hour : allPossibleHours) {

            hourLabels.add(hour.toString());

            if (occupiedHours.contains(hour)) {
                hourStatuses.add("Reserved");
            } else if (LocalDate.now(clock).isEqual(date) && LocalTime.now(clock).isAfter(hour)) {
                hourStatuses.add("Unavailable");
            } else {
                hourStatuses.add("Available");
            }
        }

        return List.of(hourLabels, hourStatuses);
    }

    public Map<String, List<List<String>>> getReservationCalendar(Long resourceId, Integer weeksLater) {
        LocalDate monday = LocalDate.now(clock)
                .plusWeeks(weeksLater)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Keep in mind: toMap doesn't allow 'null' values (leads to whole map being null due to NullPointerException)
        return IntStream.range(0, 7)
                .mapToObj(monday::plusDays)
                .collect(Collectors.toMap(
                        day -> day.getDayOfWeek().toString() + "\n" + day,
                        day -> getAllPossibleReservationHoursWithStatus(resourceId, day),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }

    public void addCalendarDataToModel(Long resourceId, Integer weeksLater, Model model) {
        try {
            model.addAttribute("calendarData", getReservationCalendar(resourceId, weeksLater));
            model.addAttribute("calendarHours", getMaxReservationHourRangeForWeek(resourceId, weeksLater));
            model.addAttribute("weeksLater", weeksLater);
        } catch (Exception e) {
            model.addAttribute("calendarData", null);
        }
    }
}
