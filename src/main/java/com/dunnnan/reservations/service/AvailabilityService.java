package com.dunnnan.reservations.service;

import com.dunnnan.reservations.constants.ReservationConstants;
import com.dunnnan.reservations.model.Availability;
import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ReservationConstants reservationConstants;

    public List<Availability> getAllAvailabilities() {
        return availabilityRepository.findAll();
    }

    public Availability getAvailabilityOfSpecificResourceForSpecificDay(Long id, LocalDate date) {
        return availabilityRepository.findByResource_IdAndDate(id, date).orElseThrow(() ->
                new RuntimeException("Resource doesn't is not available for specified date."));
    }

    public List<LocalTime> getAvailabilityTimePeriod(Long id, LocalDate date) {
        Availability availability = getAvailabilityOfSpecificResourceForSpecificDay(id, date);
        return List.of(availability.getFrom(), availability.getTo());
    }

    public boolean isReservationPeriodAvailable(
            LocalDate date, Long id, LocalTime from, LocalTime to) {
        return availabilityRepository.findByDateAndResource_IdAndFromLessThanAndToGreaterThan(date, id, to, from).isEmpty();
    }

    public boolean isAvailable(
            LocalDate date, Long resourceId
    ) {
        return !availabilityRepository.findByDateAndResource_Id(date, resourceId).isEmpty();
    }

    public void createDefaultAvailabilities(Resource resource) {
        for (int dayNumber = 0; dayNumber < reservationConstants.getMaxFrontReservationDays(); dayNumber++) {
            availabilityRepository.save(new Availability(
                            resource,
                            LocalDate.now().plusDays(dayNumber),
                            false,
                            reservationConstants.getOpeningTime(),
                            reservationConstants.getClosingTime()
                    )
            );
        }
    }

// //To ResourceService? --> Dependency Loop
//    public void resetDefaultAvailabilities() {
//        availabilityRepository.deleteAll();
//        List<Resource> allResources = resourceService.getAllResources();
//
//        for (Resource resource : allResources) {
//            for (int dayNumber = 0; dayNumber < reservationConstants.getMaxFrontReservationDays(); dayNumber++) {
//                availabilityRepository.save(new Availability(
//                                resource,
//                                LocalDate.now().plusDays(dayNumber),
//                                false,
//                                reservationConstants.getOpeningTime(),
//                                reservationConstants.getClosingTime()
//                        )
//                );
//            }
//        }
//    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledDeletePastAvailabilities() {
        availabilityRepository.deleteByDateBefore(LocalDate.now());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledCreateNewUpfrontAvailabilities() {
        List<Availability> allAvailabilities = getAllAvailabilities();

        for (Availability availability : allAvailabilities) {
            availabilityRepository.save(new Availability(
                            availability.getResource(),
                            availability.getDate().plusDays(1),
                            false,
                            reservationConstants.getOpeningTime(),
                            reservationConstants.getClosingTime()
                    )
            );
        }
    }

}
