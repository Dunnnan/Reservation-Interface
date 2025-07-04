package com.dunnnan.reservations.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="app_user")
public class AppUser {

//    INSERT INTO app_user(name, surname, email, password, phone_number, balance, user_type) VALUES
//('Mateo', 'Maldini', 'mail@com.pl', 'password', '111222333', 0, 'RESERVATOR')

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = true)
    private String surname;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false, updatable = true)
    private String password;

    @Column(nullable = false, updatable = true, name="phone_number")
    private String phoneNumber;

    @Column(nullable = false, updatable = true)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, name="user_type")
    private UserType userType = UserType.RESERVATOR;

    public AppUser() {
    }

    public AppUser(String name, String surname, String email, String password, String phoneNumber, BigDecimal balance, UserType userType) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.userType = userType;
    }

    public AppUser(String name, String surname, String email, String phoneNumber, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
