package com.dunnnan.reservations.e2e;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTests {

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

    @Given("user is on logging page")
    public void user_is_on_logging_page() {
        webDriver.get("http://localhost:8080/login");
    }

    @When("user enters correct email and password")
    public void user_enters_correct_email_and_password() {
        webDriver.findElement(By.id("email")).sendKeys("mail@com.pl");
        webDriver.findElement(By.id("password")).sendKeys("password");
    }

    @When("puts in incorrect email or password")
    public void puts_in_incorrect_email_or_password() {
        webDriver.findElement(By.id("email")).sendKeys("wrong@example.com");
        webDriver.findElement(By.id("password")).sendKeys("wrongPassword");
    }

    @When("clicks Login button")
    public void clicks_login_button() {
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("he should login and redirect to home page")
    public void he_should_login_and_redirect_to_home_page() {
        webDriverWait.until(ExpectedConditions.urlToBe("http://localhost:8080/home"));
        assertEquals("http://localhost:8080/home", webDriver.getCurrentUrl());
    }

    @Then("he should see the logging failure message")
    public void he_should_see_the_logging_failure_message() {
        WebElement error = webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("alert-danger"))
        );
        assertTrue(error.getText().contains("Wrong email or password"));
    }

    @When("clicks Create account button")
    public void clicks_create_account_button() {
        webDriver.findElement(By.linkText("Register")).click();
    }

    @Then("he should be transferred to the register page")
    public void he_should_be_transferred_to_the_register_page() {
        webDriverWait.until(ExpectedConditions.urlToBe("http://localhost:8080/register"));
        assertEquals("http://localhost:8080/register", webDriver.getCurrentUrl());
    }

}
