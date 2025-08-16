package com.dunnnan.reservations.e2e;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;

public class ReservationTests {

    @Autowired
    @Qualifier("fixedClock")
    private Clock fixedClock;

    private static final String DEFAULT_EMAIL = "mail@com.pl";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String LOGIN_URL = "http://localhost:8080/login";
    private static final String RESOURCE_URL = "http://localhost:8080/resource/1";
    private static final String HOME_URL_FRAGMENT = "/home";

    private static final String FROM = "15:00";
    private static final String TO = "20:00";
    private static final String FROM_PAST = "8:00";
    private static final String TO_PAST = "9:00";

    private String YESTERDAY_DATE;
    private String TODAY_DATE;
    private String TOMORROW_DATE;

    private WebDriver webDriver;
    private WebDriverWait webDriverWait;


    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        webDriver.manage().window().maximize();

        YESTERDAY_DATE = LocalDate.now(fixedClock).minusDays(1).toString();
        TODAY_DATE = LocalDate.now(fixedClock).toString();
        TOMORROW_DATE = LocalDate.now(fixedClock).plusDays(1).toString();

    }

    @After
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Given("User is at resource's dedicated page")
    public void user_is_at_resources_dedicated_page() {
        webDriver.get(RESOURCE_URL);
        webDriverWait.until(webDriver -> webDriver.getCurrentUrl().equalsIgnoreCase(RESOURCE_URL));
    }

    @When("User picks a present or future day of reservation")
    public void user_picks_a_present_or_future_day_of_reservation() {
        System.out.println(TOMORROW_DATE);
        webDriver.findElement(By.name("date")).sendKeys(TOMORROW_DATE);
    }

    @When("User picks a present day of reservation")
    public void user_picks_a_present_day_of_reservation() {
        System.out.println(TODAY_DATE);
        webDriver.findElement(By.name("date")).sendKeys(TODAY_DATE);
    }

    @When("User picks a past day of reservation")
    public void user_picks_a_past_day_of_reservation() {
        System.out.println(YESTERDAY_DATE);
        webDriver.findElement(By.name("date")).sendKeys(YESTERDAY_DATE);
    }

    @When("User picks a from hour of reservation")
    public void user_picks_a_from_hour_of_reservation() {
        System.out.println(FROM);
        webDriver.findElement(By.name("from")).sendKeys(FROM);
    }

    @When("User picks a to hour of reservation")
    public void user_picks_a_to_hour_of_reservation() {
        System.out.println(TO);
        webDriver.findElement(By.name("to")).sendKeys(TO);
    }

    @When("picked term is free")
    public void picked_term_is_free() {
        // Will be tested by result
    }

    @When("User clicks reserve button")
    public void user_clicks_reserve_button() {
        WebElement button = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", button);
        try {
            button.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", button);
        }
    }

    @Then("User should reserve resource")
    public void user_should_reserve_resource() {
        webDriverWait.until(webDriver -> webDriver.findElement(By.className("alert-success")).isDisplayed());
    }

    @When("picked period of time is in the past")
    public void picked_period_of_time_is_in_the_past() {
        webDriver.findElement(By.name("from")).sendKeys(FROM_PAST);
        webDriver.findElement(By.name("to")).sendKeys(TO_PAST);
    }

    @When("from hour is later than the to hour")
    public void from_hour_is_later_than_the_to_hour() {
        webDriver.findElement(By.name("from")).sendKeys(TO);
        webDriver.findElement(By.name("to")).sendKeys(FROM);
    }

    @Then("User should not reserve resource")
    public void user_should_not_reserve_resource() {
        // Will be tested by result
    }

    @Then("see the error message")
    public void see_the_error_message() {
        webDriverWait.until(webDriver -> webDriver.findElement(By.className("text-danger")).isDisplayed());
    }

    @When("picked term is full")
    public void picked_term_is_full() {
//        user_picks_a_present_or_future_day_of_reservation();
//        user_picks_a_from_hour_of_reservation();
//        user_picks_a_to_hour_of_reservation();
//        user_clicks_reserve_button();
    }

    @Then("User should not be able to reserve resource")
    public void user_should_not_be_able_to_reserve_resource() {
        // Will be tested by results
    }
}