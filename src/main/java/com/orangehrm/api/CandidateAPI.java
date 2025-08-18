package com.orangehrm.api;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

import java.time.Duration;

import static io.restassured.RestAssured.given;

public class CandidateAPI {

    private static final String BASE_URL = "https://opensource-demo.orangehrmlive.com";
    private static final SessionFilter session = new SessionFilter();
    private static String sessionId; // store cookie globally

    // Login with Selenium (headless) and extract session cookie
    public static void login(String username, String password) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            driver.get(BASE_URL + "/web/index.php/auth/login");

            WebElement usernameField = driver.findElement(By.name("username"));
            WebElement passwordField = driver.findElement(By.name("password"));
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

            usernameField.sendKeys(username);
            passwordField.sendKeys(password);
            loginButton.click();

            // Extract session cookie
            sessionId = driver.manage().getCookieNamed("orangehrm").getValue();
            System.out.println("Extracted session cookie: " + sessionId);

            // Base URL setup
            RestAssured.baseURI = BASE_URL;

        } finally {
            driver.quit();
        }
    }

    // Add candidate
    public static int addCandidate(String firstName, String lastName, String email) {
        String payload = "{\n" +
                "  \"firstName\": \"" + firstName + "\",\n" +
                "  \"lastName\": \"" + lastName + "\",\n" +
                "  \"email\": \"" + email + "\"\n" +
                "}";

        Response response = given()
                .contentType("application/json")
                .header("Cookie", "orangehrm=" + sessionId) // added header
                .filter(session)
                .body(payload)
                .when()
                .post("/web/index.php/api/v2/recruitment/candidates");

        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200, "Candidate not created!");

        int candidateId = response.jsonPath().getInt("data.id");
        System.out.println("Candidate created with ID: " + candidateId);
        return candidateId;
    }

    // Delete candidate
    public static void deleteCandidate(int candidateId) {
        String payload = "{ \"ids\": [" + candidateId + "] }";

        Response response = given()
                .header("Cookie", "orangehrm=" + sessionId)
                .filter(session)
                .contentType("application/json")
                .body(payload)
                .when()
                .delete("/web/index.php/api/v2/recruitment/candidates");

        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200, "Candidate not deleted!");
        System.out.println("Candidate deleted with ID: " + candidateId);
    }
}