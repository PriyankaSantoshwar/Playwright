package com.saucedemo.playwright.tests.login;

import com.saucedemo.playwright.base.PlaywrightTestBase;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.pages.ProductsPage;
import com.saucedemo.playwright.testdata.DemoUsers;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends PlaywrightTestBase {
    @Test
    public void validUserCanLogIn() {
        ProductsPage productsPage = new LoginPage(page)
                .loginSuccessfully(DemoUsers.STANDARD_USERNAME, DemoUsers.PASSWORD);

        Assert.assertTrue(page.url().endsWith("/inventory.html"));
        Assert.assertTrue(productsPage.getProductPrices().size() > 0,
                "The inventory should be displayed.");
    }

    @Test
     public void invalidPasswordShowsAnError() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(DemoUsers.STANDARD_USERNAME, "incorrect-password");

        Assert.assertEquals(
                loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
        Assert.assertTrue(loginPage.isDisplayed(), "The user should remain on the login page.");
    }
}
