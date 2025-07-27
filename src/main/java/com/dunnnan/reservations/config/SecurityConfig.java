package com.dunnnan.reservations.config;

import com.dunnnan.reservations.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Central configuration class for Spring Security settings.
 * <p>
 * Includes setup for UserService integration.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }


    /**
     * Configures the security filter chain.
     * <p>
     * - Disables CSRF protection (DEVELOPMENT).
     * <p>
     * - Allows frames from the same origin (DEVELOPMENT, for H2 console access).
     * <p>
     * - Permits unauthenticated access to /login and /register endpoints.
     * <p>
     * - Permits access to H2 console (DEVELOPMENT).
     * <p>
     * - Restricts access to /resource to users with EMPLOYEE role.
     * <p>
     * - Requires authentication for all other requests.
     * <p>
     * - Configures form login with email as the username parameter.
     * <p>
     * - Sets custom login page at /login.
     * <p>
     * - Redirects to /home upon successful login.
     * <p>
     * - Integrates with the provided UserService for user details.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // ONLY DEVELOPMENT
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login", "/register").permitAll()
                        // ONLY DEVELOPMENT
                        .requestMatchers("/h2-console/**").permitAll()
                        // CHANGE TO /employee/** (every employee endpoint)
                        .requestMatchers("/resource").hasRole("EMPLOYEE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .usernameParameter("email")
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home", true)
                )
                .userDetailsService(userService);
        return http.build();
    }

    /**
     * Provides the {@link AuthenticationManager} bean.
     * <p>
     * This bean exposes the AuthenticationManager built by Spring Security
     * based on the current configuration.
     * It can be injected and used elsewhere to perform programmatic authentication.
     *
     * @param authConfig the AuthenticationConfiguration provided by Spring Security
     * @return the AuthenticationManager instance
     * @throws Exception if an error occurs while retrieving the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Provides the {@link PasswordEncoder} bean.
     * <p>
     * This encoder is responsible for encoding and verifying passwords.
     *
     * @return the PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
    }


}
