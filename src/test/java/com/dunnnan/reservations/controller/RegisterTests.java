package com.dunnnan.reservations.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

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
                        .param("surname", "surname")
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
                        .param("surname", "surname")
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
                        .param("surname", "surname")
                        .param("email", "new_mail@com.pl")
                        .param("phoneNumber", "123123123")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

}
