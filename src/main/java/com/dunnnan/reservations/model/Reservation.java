package com.dunnnan.reservations.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.OffsetTime;

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

    @Column(nullable = false, updatable = false)
    private OffsetDateTime date;

    @Column(nullable = false, updatable = false, name = "start_time")
    private OffsetTime from;

    @Column(nullable = false, updatable = false, name = "end_time")
    private OffsetTime to;

    public Reservation() {
    }

    public Reservation(AppUser appUser, Resource resource, OffsetDateTime date, OffsetTime from, OffsetTime to) {
        this.appUser = appUser;
        this.resource = resource;
        this.date = date;
        this.from = from;
        this.to = to;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public OffsetTime getFrom() {
        return from;
    }

    public void setFrom(OffsetTime from) {
        this.from = from;
    }

    public OffsetTime getTo() {
        return to;
    }

    public void setTo(OffsetTime to) {
        this.to = to;
    }
}
