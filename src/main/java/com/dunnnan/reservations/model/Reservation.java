package com.dunnnan.reservations.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(nullable = false, updatable = false, name="start_time")
    private OffsetDateTime from;

    @Column(nullable = false, updatable = false, name="end_time")
    private OffsetDateTime to;

}
