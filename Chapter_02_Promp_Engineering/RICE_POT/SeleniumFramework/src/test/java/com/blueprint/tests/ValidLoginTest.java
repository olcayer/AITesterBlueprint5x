package com.blueprint.tests;

import org.testng.SkipException;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriverException;

public final class ValidLoginTest extends BaseTest {
    private String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) throw new SkipException("Missing environment variable: " + name);
        return value;
    }

    @Override
    @BeforeMethod
    public void setUp() throws WebDriverException {
        required("SALESFORCE_USERNAME");
        required("SALESFORCE_PASSWORD");
        required("SALESFORCE_SUCCESS_URL");
        required("SALESFORCE_AUTHENTICATED_XPATH");
        super.setUp();
    }

    @Test
    public void acceptsValidCredentials() throws WebDriverException {
        try {
            page.login(required("SALESFORCE_USERNAME"), required("SALESFORCE_PASSWORD"), false);
            page.awaitAuthenticatedPage(required("SALESFORCE_SUCCESS_URL"), required("SALESFORCE_AUTHENTICATED_XPATH"));
        } catch (WebDriverException exception) {
            throw new AssertionError("Could not verify authenticated Salesforce session", exception);
        }
    }
}


