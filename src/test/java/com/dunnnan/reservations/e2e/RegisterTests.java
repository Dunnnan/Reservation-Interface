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
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class RegisterTests {

    private static final String DEFAULT_NAME = "name";
    private static final String DEFAULT_SURNAME = "surname";
    private static final String DEFAULT_EMAIL = "new_mail@com.pl";
    private static final String DUPLICATE_EMAIL = "mail@com.pl";
    private static final String DEFAULT_PHONE = "123123123";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DIFFERENT_PASSWORD = "confirmPassword";
    private static final String REGISTER_URL = "http://localhost:8080/register";
    private static final String LOGIN_URL_FRAGMENT = "/login";

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
        webDriver.get(REGISTER_URL);
    }

    @When("user enters name, surname, email, phoneNumber, password, confirmPassword")
    public void user_enters_name_surname_email_phoneNumber_password_confirmPassword() {
        webDriver.findElement(By.id("name")).sendKeys(DEFAULT_NAME);
        webDriver.findElement(By.id("surname")).sendKeys(DEFAULT_SURNAME);
        webDriver.findElement(By.id("email")).sendKeys(DEFAULT_EMAIL);
        webDriver.findElement(By.id("phoneNumber")).sendKeys(DEFAULT_PHONE);
        webDriver.findElement(By.id("password")).sendKeys(DEFAULT_PASSWORD);
        webDriver.findElement(By.id("confirmPassword")).sendKeys(DEFAULT_PASSWORD);
    }

    @When("clicks Register button")
    public void clicks_register_button() {
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Given("the email is not registered yet")
    public void the_email_is_not_registered_yet() {

    }

    @Given("the email is already registered")
    public void the_email_is_already_registered() {
        webDriver.findElement(By.id("email")).clear();
        webDriver.findElement(By.id("email")).sendKeys(DUPLICATE_EMAIL);
    }

    @Given("password is the same as confirmPassword")
    public void password_is_the_same_as_confirmPassword() {
        //
    }

    @Given("password is not the same as confirmPassword")
    public void password_is_not_the_same_as_confirmPassword() {
        webDriver.findElement(By.id("confirmPassword")).clear();
        webDriver.findElement(By.id("confirmPassword")).sendKeys(DIFFERENT_PASSWORD);
    }

    @Then("he should register and redirect to login page")
    public void he_should_register_and_redirect_to_login_page() {
        webDriverWait.until(ExpectedConditions.urlContains(LOGIN_URL_FRAGMENT));
        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains(LOGIN_URL_FRAGMENT));
    }

    @Then("he should see register failure message with annotation about incorrect email")
    public void he_should_see_register_failure_message_about_email() {
        WebElement error = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-danger")));
        assertTrue(error.getText().toLowerCase().contains("email"));
    }

    @Then("he should see register failure message with annotation about incorrect password")
    public void he_should_see_register_failure_message_about_password() {
        WebElement error = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='confirmPassword']/following-sibling::div[@class='text-danger']")));
        assertTrue(error.getText().toLowerCase().contains("password"));
    }

    @When("user clicks Login button")
    public void user_clicks_login_button() {
        webDriver.findElement(By.linkText("Login")).click();
    }

    @Then("he should be transferred to the login page")
    public void he_should_be_transferred_to_the_login_page() {
        webDriverWait.until(ExpectedConditions.urlContains(LOGIN_URL_FRAGMENT));
        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains(LOGIN_URL_FRAGMENT));
    }

}
