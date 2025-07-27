package com.dunnnan.reservations.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Central configuration class for Spring MVC settings.
 * <p>
 * Includes setup for static resource handling.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configures a custom resource handler for serving uploaded image files.
     * <p>
     * To change the location of the uploaded files,
     * modify the value passed to {@code addResourceLocations()}.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/images/");
    }

}
