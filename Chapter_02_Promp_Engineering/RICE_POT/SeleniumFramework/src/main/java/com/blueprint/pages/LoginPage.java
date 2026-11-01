package com.blueprint.pages;

import java.time.Duration;
import java.net.URI;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

public final class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    @FindBy(xpath = "//input[@id='username']") private WebElement username;
    @FindBy(xpath = "//input[@id='password']") private WebElement password;
    @FindBy(xpath = "//input[@id='Login']") private WebElement submit;
    @FindBy(xpath = "//input[@id='rememberUn']") private WebElement remember;
    @FindBy(xpath = "//*[@id='error']") private WebElement error;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    public void open() throws WebDriverException {
        driver.get("https://login.salesforce.com/?locale=in");
        wait.until(ExpectedConditions.visibilityOf(username));

        wait.until(ExpectedConditions.elementToBeClickable(submit));
    }

    public void login(String user, String secret, boolean rememberUser) throws WebDriverException {
        wait.until(ExpectedConditions.visibilityOf(username)).clear();
        username.sendKeys(user);
        if (driver.findElements(By.xpath("//input[@id='password']")).stream().noneMatch(WebElement::isDisplayed)) {
            wait.until(ExpectedConditions.elementToBeClickable(submit)).click();
        }
        wait.until(ExpectedConditions.visibilityOf(password));
        password.clear();
        password.sendKeys(secret);
        if (remember.isSelected() != rememberUser) {
            wait.until(ExpectedConditions.elementToBeClickable(remember)).click();
        }
        wait.until(ExpectedConditions.elementSelectionStateToBe(remember, rememberUser));
        wait.until(ExpectedConditions.elementToBeClickable(submit)).click();
    }

    public String rejectionMessage() throws WebDriverException {
        return wait.until(ExpectedConditions.visibilityOf(error)).getText().trim();
    }

public void awaitAuthenticatedPage(String expectedUrlPrefix, String authenticatedXpath) throws WebDriverException {
        URI expected = URI.create(expectedUrlPrefix);
        if (!"https".equalsIgnoreCase(expected.getScheme()) || expected.getHost() == null
                || "login.salesforce.com".equalsIgnoreCase(expected.getHost())) {
            throw new IllegalArgumentException("SALESFORCE_SUCCESS_URL must identify the authenticated HTTPS application");
        }
        wait.until(browser -> {
            URI current = URI.create(browser.getCurrentUrl());
            return expected.getHost().equalsIgnoreCase(current.getHost())
                    && current.getPath().startsWith(expected.getPath());
        });
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(authenticatedXpath)));
    }
}



