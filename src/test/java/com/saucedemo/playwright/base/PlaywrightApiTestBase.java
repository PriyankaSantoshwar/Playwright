package com.saucedemo.playwright.base;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Map;

public abstract class PlaywrightApiTestBase {
    protected Playwright playwright;
    protected APIRequestContext apiRequest;

    @BeforeClass
    public void setUpApiRequest() {
        playwright = Playwright.create();
        apiRequest = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(System.getProperty(
                                "api.base.url",
                                "https://jsonplaceholder.typicode.com"))
                        .setExtraHTTPHeaders(Map.of("Accept", "application/json"))
                        .setTimeout(10_000));
    }

    @AfterClass(alwaysRun = true)
    public void tearDownApiRequest() {
        try {
            if (apiRequest != null) {
                apiRequest.dispose();
            }
        } finally {
            if (playwright != null) {
                playwright.close();
            }
        }
    }
}
