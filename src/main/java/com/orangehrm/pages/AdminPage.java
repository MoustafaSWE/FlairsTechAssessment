package com.orangehrm.pages;

import com.orangehrm.utilis.DriverFactory;
import io.cucumber.java.an.E;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminPage {
    private WebDriver driver = DriverFactory.getDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    private By adminTab = By.xpath("//span[text()='Admin']");
    private By[] recordCount = {
            By.xpath("//span[contains(normalize-space(.),'Record Found')]"),
            By.xpath("//span[contains(text(),'Record Found')]"),
            By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[2]/div[2]/div/span")
    };
    private By addButton = By.xpath("//button[normalize-space()='Add']");

    public void openAdminTab() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(adminTab));
        element.click();
    }

    public int getRecordCount() {
        String text = null;
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(recordCount[0]));
            text = element.getText();
        } catch (Exception e) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(recordCount[2]));
                text = element.getText();
            } catch (Exception ex) {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(recordCount[1]));
                text = element.getText();
            }

        }
        return Integer.parseInt(text.replaceAll("\\D+", ""));
    }

    public void clickAdd() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(addButton));
        element.click();
    }
}