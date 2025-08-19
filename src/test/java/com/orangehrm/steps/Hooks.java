package com.orangehrm.steps;

import com.orangehrm.utilis.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void setUp() {
        DriverFactory.initDriver().get("https://opensource-demo.orangehrmlive.com/");
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
