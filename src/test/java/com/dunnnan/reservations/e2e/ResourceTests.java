package com.dunnnan.reservations.e2e;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class ResourceTests {

    private static final String DEFAULT_EMAIL = "mail@com.pl";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String EMPLOYEE_EMAIL = "mail@com.pl";
    private static final String EMPLOYEE_PASSWORD = "password";
    private static final String LOGIN_URL = "http://localhost:8080/login";

    private static final String HOME_URL = "http://localhost:8080/home";

    private static final String RESOURCE_URL_FRAGMENT = "/resource/";
    private static final String HOME_URL_FRAGMENT = "/home";

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

    //
    @When("User clicks on a pagination control")
    public void user_clicks_on_a_pagination_control() {
        webDriver.findElement(By.cssSelector(".pagination-next")).click();
    }

    @Then("The resource grid should update with the next page of results")
    public void resource_grid_should_update_with_next_page() {
        webDriverWait.until(driver -> driver.findElement(By.cssSelector(".resource-grid")));
    }

    @When("User enters a search phrase into the search bar")
    public void user_enters_search_phrase() {
        webDriver.findElement(By.name("search")).sendKeys("example search phrase");
    }

    @When("User submits the search")
    public void user_submits_the_search() {
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("The resource grid should update to show results")
    public void resource_grid_should_update_with_search_results() {
        webDriverWait.until(driver -> driver.findElements(By.cssSelector(".resource-card")).size() > 0);
    }


    @When("User picks a filter parameter from the list")
    public void user_picks_filter_parameter() {
//        Select filterSelect = new Select(webDriver.findElement(By.name("filter")));
//        filterSelect.selectByVisibleText("CAT");
    }

    @Then("The resource grid should update to show filtered results")
    public void resource_grid_should_update_with_filtered_results() {
        webDriverWait.until(driver -> driver.findElements(By.cssSelector(".resource-card")).size() > 0);
    }

    @When("User picks a sort parameter from the list")
    public void user_picks_sort_parameter() {
//        Select sortSelect = new Select(webDriver.findElement(By.name("sort")));
//        sortSelect.selectByVisibleText("Name");
    }

    @Then("The resource grid should update to show sorted results")
    public void resource_grid_should_update_with_sorted_results() {
        webDriverWait.until(driver -> driver.findElements(By.cssSelector(".resource-card")).size() > 0);
    }


    @Given("User is authenticated as an Employee")
    public void user_is_authenticated_as_employee() {
        webDriver.get(LOGIN_URL);
        webDriver.findElement(By.name("email")).sendKeys(EMPLOYEE_EMAIL);
        webDriver.findElement(By.name("password")).sendKeys(EMPLOYEE_PASSWORD);
        webDriver.findElement(By.cssSelector("button[type='submit']")).click();
        webDriverWait.until(driver -> driver.getCurrentUrl().contains(HOME_URL_FRAGMENT));
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

        webDriver.findElement(By.name("name")).sendKeys("0_New Resource");
        webDriver.findElement(By.name("description")).sendKeys("A description of the new resource.");
        webDriver.findElement(By.name("image")).sendKeys(dummyFile.getAbsolutePath());
        webDriver.findElement(By.name("type")).sendKeys("A description of the new resource.");
    }

    @When("User submits the form")
    public void user_submits_the_form() {
        webDriver.findElement(By.id("addResourceModal button[type='submit']")).click();
    }

    @Then("The resource should be added to the grid")
    public void resource_should_be_added_to_grid() {
        webDriverWait.until(driver -> !driver.findElements(By.xpath("//*[contains(text(), '0_New Resource')]")).isEmpty());
    }

    @Then("The pop-up should close")
    public void popup_should_close() {
        webDriverWait.until(driver -> driver.findElements(By.id("addResourceModal")).isEmpty());
    }

}
