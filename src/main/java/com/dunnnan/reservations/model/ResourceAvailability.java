package com.dunnnan.reservations.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
public class ResourceAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime date;

    @Column(nullable = false, updatable = false, name="start_time")
    private OffsetDateTime from;

    @Column(nullable = false, updatable = false, name="end_time")
    private OffsetDateTime to;

}
