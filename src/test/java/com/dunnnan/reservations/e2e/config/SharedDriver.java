package com.dunnnan.reservations.e2e.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SharedDriver {

    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    public SharedDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");               // no separate sandbox
        options.addArguments("--disable-dev-shm-usage");    // use normal memory (not shared)
        options.addArguments("--remote-allow-origins=*");   // allow connection with explorer
//        options.addArguments("--headless=new");             // ??? Probably no sense

        webDriver = new ChromeDriver();
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        webDriver.manage().window().maximize();
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

}
