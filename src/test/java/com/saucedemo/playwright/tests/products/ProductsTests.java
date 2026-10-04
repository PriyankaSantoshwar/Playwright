package com.saucedemo.playwright.tests.products;

import com.saucedemo.playwright.base.PlaywrightTestBase;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.pages.ProductsPage;
import com.saucedemo.playwright.testdata.DemoUsers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductsTests extends PlaywrightTestBase {
    @Test
    public void productsCanBeSortedByPriceAscending() {
        ProductsPage productsPage = new LoginPage(page)
                .loginSuccessfully(DemoUsers.STANDARD_USERNAME, DemoUsers.PASSWORD);
        productsPage.sortPriceLowToHigh();

        List<BigDecimal> displayedPrices = productsPage.getProductPrices();
        List<BigDecimal> ascendingPrices = new ArrayList<>(displayedPrices);
        Collections.sort(ascendingPrices);

        Assert.assertEquals(displayedPrices, ascendingPrices,
                "Product prices should appear in ascending order after sorting.");
    }
}
