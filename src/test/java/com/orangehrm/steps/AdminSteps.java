package com.orangehrm.steps;

import com.orangehrm.pages.AddUserPage;
import com.orangehrm.pages.AdminPage;
import com.orangehrm.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class AdminSteps {
    LoginPage loginPage = new LoginPage();
    AdminPage adminPage = new AdminPage();
    AddUserPage addUserPage = new AddUserPage();

    int beforeCount, afterCount;
    String newUser = "testuser123";

    @Given("User logs in with username {string} and password {string}")
    public void user_logs_in_with_username_and_password(String user, String pass) {
        loginPage.login(user, pass);
    }

    @When("User navigates to Admin tab")
    public void user_navigates_to_admin_tab() {
        adminPage.openAdminTab();
        beforeCount = adminPage.getRecordCount();
    }

    @When("User adds a new user")
    public void user_adds_a_new_user() {
        adminPage.clickAdd();
        addUserPage.addUser("test", newUser, "Password@123");
    }

    @Then("User count should increase by {int}")
    public void user_count_should_increase_by(Integer increase) {
        afterCount = adminPage.getRecordCount();
        Assert.assertEquals(afterCount, beforeCount + increase,
                "Record count did not increase correctly!");
    }
}
