package com.orangehrm.pages;

import com.orangehrm.utilis.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
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
        try {

            handleUserRoleDropdown_KeyboardNavigation();

            if (!isAdminRoleSelected()) {
                handleUserRoleDropdown_DirectClick();
            }

            // Fill Employee Name
            fillEmployeeName(empName);

            // Fill Username
            fillUsername(uname);

            // Handle Status dropdown
            handleStatusDropdown();

            // Fill Passwords
            fillPasswords(pass);

            // Save the form
            saveUser();

        } catch (Exception e) {
            System.err.println("Error in addUser method: " + e.getMessage());
            throw new RuntimeException("Failed to add user", e);
        }
    }

    private void handleUserRoleDropdown_DirectClick() {
        try {
            // Scroll to element first
            WebElement roleDropdown = wait.until(ExpectedConditions.elementToBeClickable(userRole));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", roleDropdown);

            // Wait a moment after scroll
            Thread.sleep(500);

            // Click dropdown
            roleDropdown.click();

            // Wait for dropdown to be fully visible and interactable
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='listbox']")));

            // Wait for Admin option to be present and clickable
            WebElement adminOption = longWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Admin']")));

            // Scroll to admin option if needed
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", adminOption);

            // Click admin option
            adminOption.click();

            // Verify selection
            wait.until(ExpectedConditions.textToBePresentInElementLocated(userRole, "Admin"));

        } catch (Exception e) {
            System.out.println("Direct click approach failed: " + e.getMessage());

        }
    }

    private void handleUserRoleDropdown_KeyboardNavigation() {
        try {
            WebElement roleDropdown = wait.until(ExpectedConditions.elementToBeClickable(userRole));

            // Focus on the dropdown
            roleDropdown.click();

            // Use keyboard navigation instead of clicking dropdown options
            roleDropdown.sendKeys(Keys.ARROW_DOWN); // Navigate to first option
            Thread.sleep(500);

            // Keep pressing down until we find Admin (usually first or second option)
            for (int i = 0; i < 5; i++) {
                // Check current selection
                String currentText = roleDropdown.getText();
                if (currentText.contains("Admin")) {
                    roleDropdown.sendKeys(Keys.ENTER);
                    break;
                }
                roleDropdown.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(300);
            }

            // Verify selection
            wait.until(ExpectedConditions.textToBePresentInElementLocated(userRole, "Admin"));

        } catch (Exception e) {
            System.out.println("Keyboard navigation approach failed: " + e.getMessage());

        }
    }


    private boolean isAdminRoleSelected() {
        try {
            WebElement roleElement = driver.findElement(userRole);
            return roleElement.getText().contains("Admin");
        } catch (Exception e) {
            return false;
        }
    }

    private void fillEmployeeName(String empName) {
        WebElement empInput = wait.until(ExpectedConditions.elementToBeClickable(employeeName));

        // Clear field using multiple methods for reliability
        empInput.clear();
        empInput.sendKeys(Keys.CONTROL + "a");
        empInput.sendKeys(Keys.DELETE);

        // Type employee name
        empInput.sendKeys(empName);



        try {
            // Wait until the suggestions container is displayed
            By suggestionContainer = By.xpath("//div[contains(@class,'oxd-autocomplete-text-input--after')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(suggestionContainer));
        } catch (Exception e) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Use keyboard to select the first suggestion
        empInput.sendKeys(Keys.ARROW_DOWN);
        empInput.sendKeys(Keys.ENTER);
    }

    private void fillUsername(String uname) {
        WebElement userInput = wait.until(ExpectedConditions.elementToBeClickable(username));

        userInput.clear();
        userInput.sendKeys(Keys.CONTROL + "a");
        userInput.sendKeys(Keys.DELETE);

        userInput.sendKeys(uname);

    }

    private void handleStatusDropdown() {
        try {
            WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(status));

            // Try JavaScript click first
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", statusDropdown);

            // Wait for dropdown and select Enabled
            WebElement enabledOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Enabled']")));
            js.executeScript("arguments[0].click();", enabledOption);

            // Verify selection
            wait.until(ExpectedConditions.textToBePresentInElementLocated(status, "Enabled"));

        } catch (Exception e) {
            // Fallback to regular click
            WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(status));
            statusDropdown.click();

            WebElement enabledOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Enabled']")));
            enabledOption.click();

            wait.until(ExpectedConditions.textToBePresentInElementLocated(status, "Enabled"));
        }
    }

    private void fillPasswords(String pass) {
        // Fill Password
        WebElement passInput = wait.until(ExpectedConditions.elementToBeClickable(password));
        passInput.clear();
        passInput.sendKeys(pass);

        // Fill Confirm Password
        WebElement confirmPassInput = wait.until(ExpectedConditions.elementToBeClickable(confirmPassword));
        confirmPassInput.clear();
        confirmPassInput.sendKeys(pass);

        // Verify both passwords are filled
        wait.until(ExpectedConditions.attributeContains(password, "value", pass));
        wait.until(ExpectedConditions.attributeContains(confirmPassword, "value", pass));
    }

    private void saveUser() {
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));

        // Scroll to save button
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);

        // Click save button
        saveButton.click();

        // Wait for either success message or form disappearance
        try {
            // Option 1: Wait for success message
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'oxd-toast--success')]")));
        } catch (TimeoutException e) {
            // Option 2: Wait for save button to disappear
            wait.until(ExpectedConditions.invisibilityOf(saveButton));
        }
    }
}