package com.dunnnan.reservations.repository;

import com.dunnnan.reservations.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RegisterTests {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindUserByEmail() {
        AppUser appUser = new AppUser(
                "name",
                "surname",
                "new_mail@com.pl",
                "123123123",
                "password"
        );
        userRepository.save(appUser);

        Optional<AppUser> result = userRepository.findByEmail("new_mail@com.pl");
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldNotFindUserByEmail() {
        Optional<AppUser> result = userRepository.findByEmail("new_mail@com.pl");
        assertTrue(result.isEmpty());
    }

}
