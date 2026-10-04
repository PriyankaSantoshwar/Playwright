package com.saucedemo.playwright.tests.checkout;

import com.saucedemo.playwright.base.PlaywrightTestBase;
import com.saucedemo.playwright.pages.CartPage;
import com.saucedemo.playwright.pages.CheckoutPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.pages.ProductsPage;
import com.saucedemo.playwright.testdata.DemoUsers;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTests extends PlaywrightTestBase {
    @Test
    public void cartCanProceedToCustomerDetails() {
        ProductsPage productsPage = new LoginPage(page)
                .loginSuccessfully(DemoUsers.STANDARD_USERNAME, DemoUsers.PASSWORD);
        productsPage.addBackpackToCart();

        CartPage cartPage = productsPage.openCart();
        CheckoutPage checkoutPage = cartPage.checkout();

        Assert.assertTrue(checkoutPage.isCustomerDetailsFormDisplayed(),
                "Checkout should display the required customer information fields.");
    }
}
