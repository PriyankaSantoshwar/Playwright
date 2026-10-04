package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;

import java.math.BigDecimal;
import java.util.List;

public class ProductsPage extends BasePage {
    private final Locator inventoryList = page.locator(".inventory_list");
    private final Locator productPrices = page.locator(".inventory_item_price");
    private final Locator sortDropdown = page.locator("[data-test='product-sort-container']");
    private final Locator cartLink = page.locator(".shopping_cart_link");
    private final Locator menuButton = page.locator("#react-burger-menu-btn");
    private final Locator logoutLink = page.locator("#logout_sidebar_link");

    public ProductsPage(Page page) {
        super(page);
        inventoryList.waitFor();
    }

    public List<BigDecimal> getProductPrices() {
        return productPrices.allInnerTexts().stream()
                .map(price -> new BigDecimal(price.replace("$", "")))
                .toList();
    }

    public void sortPriceLowToHigh() {
        sortDropdown.selectOption("lohi");
    }

    public void addBackpackToCart() {
        page.locator("#add-to-cart-sauce-labs-backpack").click();
        page.locator(".shopping_cart_badge").waitFor();
    }

    public CartPage openCart() {
        cartLink.click();
        return new CartPage(page);
    }

    public LoginPage logout() {
        menuButton.click();
        logoutLink.click();
        return new LoginPage(page);
    }
}
