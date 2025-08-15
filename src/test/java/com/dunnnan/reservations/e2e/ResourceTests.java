package com.dunnnan.reservations.e2e;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class ResourceTests {

    private static final String DEFAULT_EMAIL = "mail@com.pl";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String EMPLOYEE_EMAIL = "ad@com.pl";
    private static final String EMPLOYEE_PASSWORD = "password";
    private static final String LOGIN_URL = "http://localhost:8080/login";

    private static final String HOME_URL = "http://localhost:8080/home";

    private static final String RESOURCE_URL_FRAGMENT = "/resource/";
    private static final String HOME_URL_FRAGMENT = "/home";

    private static final String SEARCH_PHRASE = "Larry";

    private static final String NEW_RESOURCE_NAME = "0_New Resource";
    private static final String NEW_RESOURCE_DESCRIPTION = "A description of the new resource.";
    private static final String NEW_RESOURCE_IMAGE = "";
    private static final String NEW_RESOURCE_TYPE = "A description of the new resource.";

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

    @Given("User is at the home page")
    public void user_is_at_the_home_page() {
        webDriver.get(HOME_URL);
        webDriverWait.until(driver -> driver.getCurrentUrl().equalsIgnoreCase(HOME_URL));
    }

    @When("User clicks on a resource card")
    public void user_clicks_on_a_resource_card() {
        webDriver.findElement(By.cssSelector(".resource-card")).click();
    }

    @Then("User should be redirected to the resource's dedicated page")
    public void user_should_be_redirected_to_the_resources_dedicated_page() {
        webDriverWait.until(driver -> driver.getCurrentUrl().contains(RESOURCE_URL_FRAGMENT));
    }


    @When("User clicks on a pagination control")
    public void user_clicks_on_a_pagination_control() {
        WebElement nextButton = webDriverWait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Next']"))
        );
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
        try {
            nextButton.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", nextButton);
        }
    }

    @Then("The resource grid should update with the next page of results")
    public void resource_grid_should_update_with_next_page() {
        webDriverWait.until(driver -> !driver.findElements(By.cssSelector(".resource-card")).isEmpty());
    }


    @When("User enters a search phrase into the search bar")
    public void user_enters_search_phrase() {
        webDriver.findElement(By.name("search")).sendKeys(SEARCH_PHRASE);
    }

    @When("User submits the search")
    public void user_submits_the_search() {
        // Keep in mind: this works because search submit button is the first submit button on the page
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("The resource grid should update to show results")
    public void resource_grid_should_update_with_search_results() {
        webDriverWait.until(driver -> !driver.findElements(By.cssSelector(".resource-card")).isEmpty());
    }


    @When("User picks a filter parameter from the list")
    public void user_picks_filter_parameter() {
        webDriverWait.until(driver -> !driver.findElements(By.id("type_CAT")).isEmpty());

        WebElement catCheckbox = webDriver.findElement(By.id("type_CAT"));
        if (!catCheckbox.isSelected()) {
            catCheckbox.click();
        }
        webDriver.findElement(By.id("filter"))
                .findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("The resource grid should update to show filtered results")
    public void resource_grid_should_update_with_filtered_results() {
        webDriverWait.until(driver -> !driver.findElements(By.cssSelector(".resource-card")).isEmpty());
        List<WebElement> results = webDriver.findElements(By.cssSelector(".resource-card"));
        Assertions.assertTrue(results.stream().allMatch(item -> item.getText().contains("CAT")));
    }


    @When("User picks a sort parameter from the list")
    public void user_picks_sort_parameter() {
        WebElement sortNameButton = webDriverWait.until(
                ExpectedConditions.elementToBeClickable(By.id("sortName"))
        );
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", sortNameButton);
        try {
            sortNameButton.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", sortNameButton);
        }
    }

    @Then("The resource grid should update to show sorted results")
    public void resource_grid_should_update_with_sorted_results() {
        webDriverWait.until(driver -> !driver.findElements(By.cssSelector(".resource-card")).isEmpty());
    }


    @Given("User is authenticated as an Employee")
    public void user_is_authenticated_as_employee() {
        webDriver.get(LOGIN_URL);
        webDriver.findElement(By.name("email")).sendKeys(EMPLOYEE_EMAIL);
        webDriver.findElement(By.name("password")).sendKeys(EMPLOYEE_PASSWORD);
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();
        webDriverWait.until(driver -> Objects.requireNonNull(driver.getCurrentUrl()).contains(HOME_URL_FRAGMENT));
    }

    @When("User clicks the \"Add Resource\" button")
    public void user_clicks_add_button() {
        webDriver.findElement(By.xpath("//*[contains(text(), 'Add Resource')]")).click();
    }

    @Then("The add resource pop-up should appear")
    public void add_resource_popup_should_appear() {
        webDriverWait.until(driver -> driver.findElement(By.id("addResourceModal")).isDisplayed());
    }


    @Given("The add resource pop-up is open")
    public void add_resource_popup_is_open() {
        webDriver.findElement(By.xpath("//*[contains(text(), 'Add Resource')]")).click();
        webDriverWait.until(driver -> driver.findElement(By.id("addResourceModal")).isDisplayed());
    }

    @When("User fills in valid data")
    public void user_fills_in_valid_data() throws IOException {
        File dummyFile = File.createTempFile("dummy", ".png");
        dummyFile.deleteOnExit();

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 10, 10);
        g.dispose();
        ImageIO.write(img, "png", dummyFile);

        webDriver.findElement(By.id("name")).sendKeys(NEW_RESOURCE_NAME);
        webDriver.findElement(By.id("description")).sendKeys(NEW_RESOURCE_DESCRIPTION);
        webDriver.findElement(By.id("image")).sendKeys(dummyFile.getAbsolutePath());
        webDriver.findElement(By.id("type")).sendKeys(NEW_RESOURCE_TYPE);
    }

    @When("User submits the form")
    public void user_submits_the_form() {
        webDriver.findElement(By.id("addResourceModal"))
                .findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("The pop-up should close")
    public void popup_should_close() {
        webDriverWait.until(driver -> !driver.findElement(By.id("addResourceModal")).isDisplayed());
    }

    @Then("The resource should be added to the grid")
    public void resource_should_be_added_to_grid() {
        webDriver.findElement(By.name("search")).sendKeys(NEW_RESOURCE_NAME);
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();

        webDriverWait.until(driver -> driver.findElement(By.cssSelector(".resource-card")).isDisplayed());
        List<WebElement> results = webDriver.findElements(By.cssSelector(".resource-card"));
        Assertions.assertTrue(results.stream().anyMatch(item -> item.getText().contains(NEW_RESOURCE_NAME)));
    }

}
