package com.dunnnan.reservations.e2e;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
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
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class RegisterTests {

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

    @Given("user is on register page")
    public void user_is_on_register_page() {
        webDriver.get("http://localhost:8080/register");
    }

    @When("user enters name, surname, email, phoneNumber, password, confirmPassword")
    public void user_enters_name_surname_email_phoneNumber_password_confirmPassword() {
        webDriver.findElement(By.id("name")).sendKeys("name");
        webDriver.findElement(By.id("surname")).sendKeys("surname");
        webDriver.findElement(By.id("email")).sendKeys("new_mail@com.pl");
        webDriver.findElement(By.id("phoneNumber")).sendKeys("123123123");
        webDriver.findElement(By.id("password")).sendKeys("password");
        webDriver.findElement(By.id("confirmPassword")).sendKeys("password");
    }

    @And("clicks Register button")
    public void clicks_register_button() {
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @And("the email is not registered yet")
    public void the_email_is_not_registered_yet() {
        //
    }

    @And("the email is already registered")
    public void the_email_is_already_registered() {
        webDriver.findElement(By.id("email")).clear();
        webDriver.findElement(By.id("email")).sendKeys("mail@com.pl");
    }

    @And("password is the same as confirmPassword")
    public void password_is_the_same_as_confirmPassword() {
        //
    }

    @And("password is not the same as confirmPassword")
    public void password_is_not_the_same_as_confirmPassword() {
        webDriver.findElement(By.id("password")).clear();
        webDriver.findElement(By.id("password")).sendKeys("password");
        webDriver.findElement(By.id("confirmPassword")).clear();
        webDriver.findElement(By.id("confirmPassword")).sendKeys("confirmPassword");

    }

    @Then("he should register and redirect to home page")
    public void he_should_register_and_redirect_to_home_page() {
        webDriverWait.until(ExpectedConditions.urlContains("/home"));
        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains("/home"));
    }

    @Then("he should see register failure message with annotation about incorrect email")
    public void he_should_see_register_failure_message_about_email() {
        WebElement error = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-danger")));
        assertTrue(error.getText().toLowerCase().contains("email"));
    }

    @Then("he should see register failure message with annotation about incorrect password")
    public void he_should_see_register_failure_message_about_password() {
        WebElement error = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-danger")));
        assertTrue(error.getText().toLowerCase().contains("password"));
    }

    @When("user clicks Login button")
    public void user_clicks_login_button() {
        webDriver.findElement(By.linkText("Login")).click();
    }

    @Then("he should be transferred to the login page")
    public void he_should_be_transferred_to_the_login_page() {
        webDriverWait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains("/login"));
    }

}
