package com.orangehrm.pages;

import com.orangehrm.utilis.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddUserPage {
    private WebDriver driver = DriverFactory.getDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    private Actions actions = new Actions(driver);

    private By userRole = By.xpath("(//div[@class='oxd-select-text-input'])[1]");
    private By employeeName = By.xpath("//input[@placeholder='Type for hints...']");
    private By username = By.xpath("(//input[@class='oxd-input oxd-input--active'])[2]");
    private By status = By.xpath("(//div[@class='oxd-select-text-input'])[2]");
    private By password = By.xpath("(//input[@type='password'])[1]");
    private By confirmPassword = By.xpath("(//input[@type='password'])[2]");
    private By saveBtn = By.xpath("//button[normalize-space()='Save']");

    public void addUser(String empName, String uname, String pass) {
        // Open User Role dropdown
        WebElement roleDropdown = wait.until(ExpectedConditions.elementToBeClickable(userRole));
        actions.moveToElement(roleDropdown).click().perform();

        // Wait until dropdown options are loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='listbox']")));

        // Select "Admin" option
        WebElement adminOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Admin']")));
        actions.moveToElement(adminOption).click().perform();

        // Ensure role is selected
        wait.until(ExpectedConditions.textToBePresentInElementLocated(userRole, "Admin"));

        // Fill Employee Name
        WebElement empInput = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeName));
        empInput.clear();
        empInput.sendKeys(empName);

        // Fill Username
        WebElement userInput = wait.until(ExpectedConditions.visibilityOfElementLocated(username));
        userInput.clear();
        userInput.sendKeys(uname);

        // Select Status with Actions
        WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(status));
        actions.moveToElement(statusDropdown).click().perform();
        WebElement enabledOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Enabled']")));
        actions.moveToElement(enabledOption).click().perform();
        // Ensure status is selected
        wait.until(ExpectedConditions.textToBePresentInElementLocated(status, "Enabled"));

        // Fill Passwords
        WebElement passInput = wait.until(ExpectedConditions.visibilityOfElementLocated(password));
        passInput.clear();
        passInput.sendKeys(pass);

        WebElement confirmPassInput = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPassword));
        confirmPassInput.clear();
        confirmPassInput.sendKeys(pass);

        // Save with Actions
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
        actions.moveToElement(saveButton).click().perform();
        // Ensure form is submitted (wait for save button to disappear OR success message to appear)
        wait.until(ExpectedConditions.invisibilityOf(saveButton));
    }
}