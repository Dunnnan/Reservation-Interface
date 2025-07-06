package com.dunnnan.reservations.service;

import com.dunnnan.reservations.model.AppUser;
import com.dunnnan.reservations.model.dto.AppUserDto;
import com.dunnnan.reservations.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RegisterTests {

    private static final String testEmail = "new_mail@com.pl";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        AppUser appUser = new AppUser(
                "name",
                "surname",
                testEmail,
                "123123123",
                "password"
        );
        userRepository.save(appUser);
    }

    @Test
    public void shouldLoadUserByEmail() {
        var userDetails = userService.loadUserByUsername(testEmail);
        assertEquals(testEmail, userDetails.getUsername());
    }

    @Test
    public void emailShouldExists() {
        assertTrue(userService.emailExists(testEmail));
    }

    @Test
    public void emailShouldNotExist() {
        assertFalse(userService.emailExists("notEvenCorrectEmail"));
    }

    @Test
    public void shouldRegisterNewUser() {
        AppUserDto newUser = new AppUserDto();
        newUser.setName("name");
        newUser.setSurname("surname");
        newUser.setEmail("new_mail_2@com.pl");
        newUser.setPhoneNumber("123123123");
        newUser.setPassword("password");
        newUser.setConfirmPassword("confirmPassword");

        userService.registerUser(newUser);
        assertTrue(userRepository.findByEmail("new_mail_2@com.pl").isPresent());
    }

}
