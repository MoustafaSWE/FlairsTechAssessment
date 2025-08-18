package com.orangehrm.pages;

import com.orangehrm.utilis.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver = DriverFactory.getDriver();

    private By username = By.name("username");
    private By password = By.name("password");
    private By loginBtn = By.xpath("//button[@type='submit']");

    public void login(String user, String pass) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginBtn).click();
    }
}
