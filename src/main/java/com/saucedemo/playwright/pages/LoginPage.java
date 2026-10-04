package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;

public class LoginPage extends BasePage {
    private final Locator usernameField = page.locator("[data-test='username']");
    private final Locator passwordField = page.locator("[data-test='password']");
    private final Locator loginButton = page.locator("[data-test='login-button']");
    private final Locator errorMessage = page.locator("[data-test='error']");

    public LoginPage(Page page) {
        super(page);
        usernameField.waitFor();
    }

    public void login(String username, String password) {
        usernameField.fill(username);
        passwordField.fill(password);
        loginButton.click();
    }

    public ProductsPage loginSuccessfully(String username, String password) {
        login(username, password);
        return new ProductsPage(page);
    }

    public boolean isDisplayed() {
        return loginButton.isVisible() && usernameField.isVisible();
    }

    public String getErrorMessage() {
        return errorMessage.innerText();
    }
}
