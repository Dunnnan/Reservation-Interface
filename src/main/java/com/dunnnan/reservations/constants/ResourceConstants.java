package com.dunnnan.reservations.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for resource parameters or settings.
 * <p>
 * Contains all configurable parameters related to the resource service.
 */
@Configuration
@ConfigurationProperties(prefix = "resource")
public class ResourceConstants {

    /**
     * Available sort options for resources.
     * <br>
     * Option must be a field in @Resource class.
     * <br>
     * Each unique sort option should have its own formula in @ResourceValidator
     */
    private List<String> sortOptions = List.of(
            "Name",
            "Type",
            "Added"
    );

    public List<String> getSortOptions() {
        return sortOptions;
    }

    public void setSortOptions(List<String> sortOptions) {
        this.sortOptions = sortOptions;
    }
}
