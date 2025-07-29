package com.dunnnan.reservations.repository;

import com.dunnnan.reservations.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndResource_IdAndFromLessThanAndToGreaterThan(
            LocalDate date, Long id, LocalTime to, LocalTime from);

    List<Reservation> findByDateAndResource_Id(
            LocalDate date, Long id
    );

}
