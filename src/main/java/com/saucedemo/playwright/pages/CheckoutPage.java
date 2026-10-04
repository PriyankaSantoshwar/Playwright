package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;

public class CheckoutPage extends BasePage {
    private final Locator firstNameField = page.locator("[data-test='firstName']");
    private final Locator lastNameField = page.locator("[data-test='lastName']");
    private final Locator postalCodeField = page.locator("[data-test='postalCode']");

    public CheckoutPage(Page page) {
        super(page);
        firstNameField.waitFor();
    }

    public boolean isCustomerDetailsFormDisplayed() {
        return firstNameField.isVisible()
                && lastNameField.isVisible()
                && postalCodeField.isVisible();
    }
}
