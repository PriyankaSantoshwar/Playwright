package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;

public class CartPage extends BasePage {
    private final Locator itemNames = page.locator(".cart_item .inventory_item_name");
    private final Locator checkoutButton = page.locator("[data-test='checkout']");

    public CartPage(Page page) {
        super(page);
        page.locator(".cart_list").waitFor();
    }

    public boolean containsBackpack() {
        return itemNames.allInnerTexts().stream()
                .anyMatch(item -> item.equals("Sauce Labs Backpack"));
    }

    public int getItemCount() {
        return itemNames.count();
    }

    public CheckoutPage checkout() {
        checkoutButton.click();
        return new CheckoutPage(page);
    }
}
