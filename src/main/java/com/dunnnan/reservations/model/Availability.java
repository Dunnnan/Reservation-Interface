package com.dunnnan.reservations.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @Column(nullable = false, updatable = true, name = "is_closed")
    private boolean isClosed;

    @Column(nullable = false, updatable = true, name = "start_time")
    private LocalTime from;

    @Column(nullable = false, updatable = true, name = "end_time")
    private LocalTime to;


    public Availability() {
    }

    public Availability(Resource resource, LocalDate date, boolean isClosed, LocalTime from, LocalTime to) {
        this.resource = resource;
        this.date = date;
        this.isClosed = isClosed;
        this.from = from;
        this.to = to;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }
}
