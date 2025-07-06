package com.dunnnan.reservations.controller;


import com.dunnnan.reservations.model.Resource;
import com.dunnnan.reservations.model.ResourceType;
import com.dunnnan.reservations.repository.ResourceRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ResourcesTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void init(@Autowired ResourceRepository resourceRepository) {
        resourceRepository.save(new Resource("Cat", "Very cute cat", "image1", ResourceType.CAT));
        resourceRepository.save(new Resource("Dog", "Very cute dog", "image2", ResourceType.DOG));
        resourceRepository.save(new Resource("Bear", "Very cute bear", "image3", ResourceType.BEAR));
    }

    @Test
    public void shouldAccessHomePage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void shouldAccessSpecifiedResourcePage() throws Exception {
        mockMvc.perform(get("/resource/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("resource-detail"));
    }

    @Test
    public void shouldNavigateBetweenPagesOfResource() throws Exception {
        mockMvc.perform(get("/home?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("resources"));
    }

    @Test
    public void shouldSearchForASpecificResource() throws Exception {
        mockMvc.perform(get("/home?search=cat"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("resources"));
    }

    @Test
    public void shouldFilterResourceByParameter() throws Exception {
        mockMvc.perform(get("/home?type=cat"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("resources"));
    }

    @Test
    public void shouldSortResourceByParameter() throws Exception {
        mockMvc.perform(get("/home?sort=name,asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("resources"));
    }

    @Test
    public void shouldTheAddResourceFormExists() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("resource"));
    }

    @Test
    public void shouldSubmitTheAddResourceForm() throws Exception {
        mockMvc.perform(post("/resource/add")
                    .param("name", "name")
                    .param("description", "description")
                    .param("imageName", "imageName")
                    .param("type", "CAT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    public void shouldAccessRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void shouldNotRegisterDueToIncorrectEmail() throws Exception {
        mockMvc.perform(post("/register")
                        .param("name", "name")
                        .param("surname","surname")
                        .param("email", "wrong_email")
                        .param("phoneNumber", "123123123")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    public void shouldNotRegisterDueToIncorrectPassword() throws Exception {
        mockMvc.perform(post("/register")
                        .param("name", "name")
                        .param("surname","surname")
                        .param("email", "mail@com.pl")
                        .param("phoneNumber", "123123123")
                        .param("password", "password")
                        .param("confirmPassword", "different_password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "confirmPassword"));
    }

    @Test
    public void shouldRegisterDueToCorrectCredentials() throws Exception {
        mockMvc.perform(post("/register")
                        .param("name", "name")
                        .param("surname","surname")
                        .param("email", "new_mail@com.pl")
                        .param("phoneNumber", "123123123")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

}
