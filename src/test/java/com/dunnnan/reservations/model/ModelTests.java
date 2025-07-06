package com.dunnnan.reservations.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ModelTests {

    @Test
    void emptyUserShouldBeCreated() {
        AppUser user = new AppUser();
        assertThat(user).isNotNull();
    }

    @Test
    void userShouldBeCreatedFullConstructor() {
        AppUser user = new AppUser(
                "Mateo",
                "Maldini",
                "yes@mail.com",
                "password",
                "111222333",
                BigDecimal.ZERO,
                UserType.RESERVATOR);

        assertThat(user.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(user.getUserType()).isEqualTo(UserType.RESERVATOR);
    }

    @Test
    void userShouldBeCreatedMediumConstructor() {
        AppUser user = new AppUser(
                "Mateo",
                "Maldini",
                "yes@mail.com",
                "password",
                "111222333");

        assertThat(user.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(user.getUserType()).isEqualTo(UserType.RESERVATOR);
    }

    @Test
    void enumIsCorrectlyConvertedToString() {
        assertThat(UserType.RESERVATOR.toString()).isEqualTo("RESERVATOR");
        assertThat(UserType.EMPLOYEE.toString()).isEqualTo("EMPLOYEE");
    }

}
