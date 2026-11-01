package com.blueprint.tests;

import com.blueprint.pages.LoginPage;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.testng.*;
import org.testng.annotations.*;
import java.io.ByteArrayInputStream;

public abstract class BaseTest {
    protected WebDriver driver;
    protected LoginPage page;

    @BeforeTest
    public void configure() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
    }

    @BeforeMethod
    public void setUp() throws WebDriverException {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) options.addArguments("--headless=new");
        options.addArguments("--window-size=1440,1000");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(60));
        page = new LoginPage(driver);
        page.open();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver == null) return;
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                Allure.addAttachment("Failure screenshot", "image/png", new ByteArrayInputStream(
                        ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)), ".png");
            }
        } catch (WebDriverException exception) {
            Allure.addAttachment("Screenshot failure", exception.getClass().getSimpleName());
        } finally {
            driver.quit();
            driver = null;
        }
    }
}



