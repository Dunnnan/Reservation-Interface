package com.dunnnan.reservations.e2e.tests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTests {

    private static final String LOGIN_URL = "http://localhost:8080/login";
    private static final String REGISTER_URL = "http://localhost:8080/register";
    private static final String HOME_URL = "http://localhost:8080/home";
    private static final String DEFAULT_EMAIL = "new_mail@com.pl";
    private static final String WRONG_EMAIL = "mail@com.pl";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrongPassword";

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private WebDriverWait webDriverWait;


    @Given("user is on logging page")
    public void user_is_on_logging_page() {
        webDriver.get(LOGIN_URL);
    }

    @When("user enters correct email and password")
    public void user_enters_correct_email_and_password() {
        webDriver.findElement(By.id("email")).sendKeys(DEFAULT_EMAIL);
        webDriver.findElement(By.id("password")).sendKeys(DEFAULT_PASSWORD);
    }

    @When("puts in incorrect email or password")
    public void puts_in_incorrect_email_or_password() {
        webDriver.findElement(By.id("email")).sendKeys(WRONG_EMAIL);
        webDriver.findElement(By.id("password")).sendKeys(WRONG_PASSWORD);
    }

    @When("clicks Login button")
    public void clicks_login_button() {
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("he should login and redirect to home page")
    public void he_should_login_and_redirect_to_home_page() {
        webDriverWait.until(ExpectedConditions.urlToBe(HOME_URL));
        assertEquals(HOME_URL, webDriver.getCurrentUrl());
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
        webDriverWait.until(ExpectedConditions.urlToBe(REGISTER_URL));
        assertEquals(REGISTER_URL, webDriver.getCurrentUrl());
    }

}
