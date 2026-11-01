package com.blueprint.tests;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.Test;
public final class InvalidLoginTest extends BaseTest {
    @Test
    public void rejectsInvalidCredentials() throws WebDriverException {
        try {
            page.login("blueprint.invalid@example.invalid", "Invalid-Login-Only-42!", true);
            Assert.assertTrue(page.rejectionMessage().contains("Please check your username and password."), "Login must reject invalid credentials");
            Assert.assertEquals(java.net.URI.create(driver.getCurrentUrl()).getHost(), "login.salesforce.com");
        } catch (WebDriverException exception) {
            throw new AssertionError("Could not verify invalid Salesforce login", exception);
        }
    }

}




