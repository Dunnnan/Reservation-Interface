package com.dunnnan.reservations.e2e.hooks;

import com.dunnnan.reservations.e2e.config.SharedDriver;
import io.cucumber.java.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;

public class DriverHooks {

    private static SharedDriver sharedDriver;

    @Autowired
    public DriverHooks(SharedDriver sharedDriver) {
        DriverHooks.sharedDriver = sharedDriver;
    }

    @AfterAll
    public static void closeDriver() {
        if (sharedDriver != null && sharedDriver.getWebDriver() != null) {
            System.out.println("Closing webDriver (Hook)");
            sharedDriver.getWebDriver().quit();
        }
    }

}
