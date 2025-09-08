package com.dunnnan.reservations.controller;

import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.service.AvailabilityService;
import com.dunnnan.reservations.service.ResourceService;
import com.dunnnan.reservations.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Clock;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AvailabilityTests {

    private Long resourceId;
    private UserDetails userDetails;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    @Qualifier("fixedClock")
    private Clock fixedClock;

    @BeforeAll
    public void init() {
        userDetails = userService.loadUserByUsername("mail@com.pl");
        resourceId = resourceService.addResourceMock(
                new Resource(
                        "Cat",
                        "Very cute cat",
                        "image1",
                        ResourceType.CAT
                )
        ).getId();
    }

}
