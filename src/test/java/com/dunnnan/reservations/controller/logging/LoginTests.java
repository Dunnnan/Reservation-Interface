package com.dunnnan.reservations.controller.logging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldAccessLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void shouldNotLoginDueToIncorrectCredentials() throws Exception {
        mockMvc.perform(
                formLogin()
                    .loginProcessingUrl("/login")
                    .user("email", "wrong@email.com")
                    .password("wrong_password")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void shouldLoginDueToCorrectCredentials() throws Exception {
        mockMvc.perform(
                formLogin()
                        .loginProcessingUrl("/login")
                        .user("email","mail@com.pl")
                        .password("password")
            )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

//    @Test
//    public void shouldAccessLoginPage() {
//        ResponseEntity<String> response = restTemplate.getForEntity("/login", String.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }

//    @Test
//    public void shouldRedirectToRegisterPage() {
//        ResponseEntity<String> response = restTemplate.getForEntity("/login", String.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TEMPORARY_REDIRECT);
//    }

}
