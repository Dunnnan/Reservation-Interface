package com.dunnnan.reservations.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AppUserDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not correctly formatted")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min=9, max=9, message = "Phone number must have 9 characters")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    // Methods

    public AppUserDto() {
    }

    public @NotBlank(message = "Name is required") String getName() {
        return name;
    }

    public @NotBlank(message = "Surname is required") String getSurname() {
        return surname;
    }

    public @NotBlank(message = "Email is required") @Email String getEmail() {
        return email;
    }

    public @NotBlank(message = "Phone number is required") @Size(min = 9, max = 9, message = "Phone number must have 9 characters") String getPhoneNumber() {
        return phoneNumber;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public @NotBlank(message = "Confirm password is required") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setName(@NotBlank(message = "Name is required") String name) {
        this.name = name;
    }

    public void setSurname(@NotBlank(message = "Surname is required") String surname) {
        this.surname = surname;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email String email) {
        this.email = email;
    }

    public void setPhoneNumber(@NotBlank(message = "Phone number is required") @Size(min = 9, max = 9, message = "Phone number must have 9 characters") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }

    public void setConfirmPassword(@NotBlank(message = "Confirm password is required") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
