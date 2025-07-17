package com.dunnnan.reservations.service;

import com.dunnnan.reservations.config.ReservationConfig;
import com.dunnnan.reservations.model.Availability;
import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ReservationConfig reservationConfig;

    public List<Availability> getAllAvailabilities() {
        return availabilityRepository.findAll();
    }

    public void createDefaultAvailabilities(Resource resource) {
        for (int dayNumber = 0; dayNumber < reservationConfig.getMaxFrontReservationDays(); dayNumber++) {
            availabilityRepository.save(new Availability(
                            resource,
                            LocalDate.now().plusDays(dayNumber),
                            false,
                            reservationConfig.getOpeningTime(),
                            reservationConfig.getClosingTime()
                    )
            );
        }
    }

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
                            reservationConfig.getOpeningTime(),
                            reservationConfig.getClosingTime()
                    )
            );
        }
    }

}
