package com.saucedemo.playwright.tests.logout;

import com.saucedemo.playwright.base.PlaywrightTestBase;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.pages.ProductsPage;
import com.saucedemo.playwright.testdata.DemoUsers;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LogoutTests extends PlaywrightTestBase {
    @Test
    public void loggedInUserCanLogOut() {
        ProductsPage productsPage = new LoginPage(page)
                .loginSuccessfully(DemoUsers.STANDARD_USERNAME, DemoUsers.PASSWORD);

        LoginPage loginPage = productsPage.logout();

        Assert.assertTrue(loginPage.isDisplayed(), "Logout should return the user to the login page.");
        Assert.assertTrue(page.url().endsWith("/"));
    }
}
