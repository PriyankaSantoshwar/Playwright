package com.saucedemo.playwright.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class PlaywrightTestBase {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        boolean headless = Boolean.parseBoolean(System.getProperty("playwright.headless", "false"));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1440, 1000));
        context.setDefaultTimeout(10_000);
        page = context.newPage();
        page.navigate(System.getProperty("site.url", "https://www.saucedemo.com/"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (page != null && !result.isSuccess()) {
                saveFailureScreenshot(result.getName());
            }
        } finally {
            if (context != null) {
                context.close();
            }
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
        }
    }

    private void saveFailureScreenshot(String testName) {
        try {
            Path screenshotDirectory = Path.of("target", "screenshots");
            Files.createDirectories(screenshotDirectory);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            Path screenshot = screenshotDirectory.resolve("%s-%s.png".formatted(testName, timestamp));
            page.screenshot(new Page.ScreenshotOptions().setPath(screenshot).setFullPage(true));
            System.err.println("Saved failure screenshot: " + screenshot);
        } catch (Exception exception) {
            System.err.println("Could not save failure screenshot for " + testName + ": " + exception.getMessage());
        }
    }

}
