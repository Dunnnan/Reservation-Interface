package com.dunnnan.reservations.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Central configuration class for Spring Pagination settings.
 * <p>
 * Includes setup of page parameters.
 */

@Configuration
@ConfigurationProperties(prefix = "pagination")
public class PaginationConfig {

    /**
     * Parameter for default number of elements per page.
     */
    private int defaultPageSize = 2;

    /**
     * Parameter for maximum number of elements per page (that user can't cross).
     */
    private int maxPageSize = 20;


    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }
}
