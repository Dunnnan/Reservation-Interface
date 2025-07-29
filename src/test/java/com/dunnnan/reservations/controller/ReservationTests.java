package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.TestConfig;
import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.repository.ResourceRepository;
import com.dunnnan.reservations.service.AvailabilityService;
import com.dunnnan.reservations.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import(TestConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationTests {

    private static UserDetails userDetails;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    MockMvc mockMvc;
    private Long resourceId;
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    @Qualifier("fixedClock")
    private Clock fixedClock;

    @BeforeAll
    public void init() {
        userDetails = userService.loadUserByUsername("mail@com.pl");

        Resource resource = resourceRepository.save(new Resource("Cat", "Very cute cat", "image1", ResourceType.CAT));
        resourceId = resource.getId();

        availabilityService.createDefaultAvailabilities(resource);
    }

    @Test
    public void shouldReservationModelExists() throws Exception {
        mockMvc.perform(get("/resource/1")
                        .with(user("mail@com.pl").roles("RESERVATOR"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("resource-detail"))
                .andExpect(model().attributeExists("reservation"));
    }

    @Test
    public void shouldRedirectToHome() throws Exception {
        mockMvc.perform(post("/reserve")
                        .param("resourceId", "1000")
                        .param("date", LocalDate.now().toString())
                        .param("from", "15:00")
                        .param("to", "20:00")
                        .with(user(userDetails))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    public void shouldReserveResourceSuccessfully() throws Exception {
        mockMvc.perform(post("/reserve")
                        .param("resourceId", String.valueOf(resourceId)) // NOTE: This works only due to current state of db (needs separable db)
                        .param("date", LocalDate.now().toString())
                        .param("from", "15:00")
                        .param("to", "20:00")
                        .with(user(userDetails))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/resource/" + resourceId))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    public void shouldFailToReserveDueToReservationPeriodBeingInThePast() throws Exception {
        LocalTime now = LocalTime.now(fixedClock);
        LocalTime from = now.minusHours(1);
        LocalTime to = now.plusHours(1);

        mockMvc.perform(post("/reserve")
                        .param("resourceId", String.valueOf(resourceId))
                        .param("date", LocalDate.now().toString())
                        .param("from", String.valueOf(from))
                        .param("to", String.valueOf(to))
                        .with(user(userDetails))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("resource-detail"))
                .andExpect(content().string(containsString("text-danger")));
    }

    @Test
    public void shouldFailToReserveDueToFromBeingLaterThanTo() throws Exception {
        mockMvc.perform(post("/reserve")
                        .param("resourceId", String.valueOf(resourceId))
                        .param("date", LocalDate.now().toString())
                        .param("from", "20:00")
                        .param("to", "15:00")
                        .with(user(userDetails))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("resource-detail"))
                .andExpect(content().string(containsString("text-danger")));
    }

    @Test
    public void shouldFailToReserveDueToDateEarlierThanToday() throws Exception {
        mockMvc.perform(post("/reserve")
                        .param("resourceId", String.valueOf(resourceId))
                        .param("date", LocalDate.now().minusDays(1).toString())
                        .param("from", "15:00")
                        .param("to", "15:00")
                        .with(user(userDetails))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("resource-detail"))
                .andExpect(content().string(containsString("text-danger")));
    }

    @Test
    public void shouldFailToReserveDueToNoAvailability() throws Exception {
        mockMvc.perform(post("/reserve")
                        .param("resourceId", "1")
                        .param("date", LocalDate.now().toString())
                        .param("from", "15:00")
                        .param("to", "20:00")
                        .with(user(userDetails))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("resource-detail"))
                .andExpect(content().string(containsString("text-danger")));
    }

    @Test
    public void shouldFailToReserveDueToPeriodAlreadyOccupied() throws Exception {
        mockMvc.perform(post("/reserve")
                        .param("resourceId", String.valueOf(resourceId))
                        .param("date", LocalDate.now().toString())
                        .param("from", "15:00")
                        .param("to", "20:00")
                        .with(user(userDetails))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/resource/" + resourceId))
                .andExpect(flash().attributeExists("successMessage"));

        mockMvc.perform(post("/reserve")
                        .param("resourceId", String.valueOf(resourceId))
                        .param("date", LocalDate.now().toString())
                        .param("from", "15:00")
                        .param("to", "20:00")
                        .with(user(userDetails))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("resource-detail"))
                .andExpect(content().string(containsString("text-danger")));
    }

}
