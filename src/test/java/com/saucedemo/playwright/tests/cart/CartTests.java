package com.saucedemo.playwright.tests.cart;

import com.saucedemo.playwright.base.PlaywrightTestBase;
import com.saucedemo.playwright.pages.CartPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.pages.ProductsPage;
import com.saucedemo.playwright.testdata.DemoUsers;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTests extends PlaywrightTestBase {
    @Test
    public void addedProductAppearsInCart() {
        ProductsPage productsPage = new LoginPage(page)
                .loginSuccessfully(DemoUsers.STANDARD_USERNAME, DemoUsers.PASSWORD);
        productsPage.addBackpackToCart();

        CartPage cartPage = productsPage.openCart();

        Assert.assertEquals(cartPage.getItemCount(), 1);
        Assert.assertTrue(cartPage.containsBackpack(), "The backpack should be present in the cart.");
    }
}
