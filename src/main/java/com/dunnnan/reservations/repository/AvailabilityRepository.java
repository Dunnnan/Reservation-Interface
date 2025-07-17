package com.dunnnan.reservations.repository;

import com.dunnnan.reservations.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    void deleteByDateBefore(LocalDate date);

    List<Availability> findFirstByOrderByDateDesc();
}
