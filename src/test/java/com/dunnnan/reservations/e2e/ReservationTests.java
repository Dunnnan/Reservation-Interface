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

import java.time.Duration;
import java.time.LocalDate;

public class ReservationTests {

    private static final String DEFAULT_EMAIL = "mail@com.pl";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String LOGIN_URL = "http://localhost:8080/login";

    private static final String RESOURCE_URL = "http://localhost:8080/resource/1";
    private static final String HOME_URL_FRAGMENT = "/home";

    private static final String TOMORROW_DATE = LocalDate.now().plusDays(1).toString();
    private static final String FROM = "15:00";
    private static final String TO = "20:00";

    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        webDriver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Given("User is authenticated")
    public void user_is_authenticated() {
        webDriver.get(LOGIN_URL);

        webDriver.findElement(By.name("email")).sendKeys(DEFAULT_EMAIL);
        webDriver.findElement(By.name("password")).sendKeys(DEFAULT_PASSWORD);
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();

        webDriverWait.until(webDriver -> webDriver.getCurrentUrl().contains(HOME_URL_FRAGMENT));
    }

    @Given("User is at resource's dedicated page")
    public void user_is_at_resources_dedicated_page() {
        webDriver.get(RESOURCE_URL);
        webDriverWait.until(webDriver -> webDriver.getCurrentUrl().equalsIgnoreCase(RESOURCE_URL));
    }

    @When("User picks a certain day of reservation")
    public void user_picks_a_certain_day_of_reservation() {
        System.out.println(TOMORROW_DATE);
//        webDriver.findElement(By.name("date")).sendKeys(TOMORROW_DATE);
        WebElement dateInput = webDriver.findElement(By.name("date"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value = arguments[1];", dateInput, TOMORROW_DATE);
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
        // TODO
    }

    @When("day of reservation is in the past")
    public void day_of_reservation_is_in_the_past() {
        // TODO
    }

    @When("from hour is later than the to hour")
    public void from_hour_is_later_than_the_to_hour() {
        // TODO
    }

    @Then("User should not reserve resource")
    public void user_should_not_reserve_resource() {
        // TODO
    }

    @Then("see the error message")
    public void see_the_error_message() {
        // TODO
    }

    @When("picked term is full")
    public void picked_term_is_full() {
        // TODO
    }

    @When("User picks a day of reservation in the past")
    public void user_picks_a_day_of_reservation_in_the_past() {
        // TODO
    }

    @When("User picks a from hour later than the to hour")
    public void user_picks_a_from_hour_later_than_the_to_hour() {
        // TODO
    }

    @Then("User should not be able to reserve resource")
    public void user_should_not_be_able_to_reserve_resource() {
        // TODO
    }
}