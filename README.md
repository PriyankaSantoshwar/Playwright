# SauceDemo UI Automation - Playwright Java

A standalone Playwright for Java + TestNG portfolio project automating realistic e-commerce journeys on [SauceDemo](https://www.saucedemo.com), a public application designed for software testing practice.

This project is intentionally separate from the Selenium project. Both automate the same website and feature flows, making the two repositories useful for comparing WebDriver-based Selenium with Playwright's browser, context, page, locator, and auto-waiting model.

## What it demonstrates

- Playwright Java Page Object Model
- Locator-based interactions with built-in auto-waiting
- Isolated browser context and page per TestNG test
- Positive and negative login coverage
- Product sorting, cart validation, checkout form, and logout flows
- Failure screenshots saved under `target/screenshots`
- Headless CI execution through GitHub Actions

## Technology

Java 17 · Maven · Playwright Java 1.63 · TestNG 7

## Scenarios

| Area | Scenario |
| --- | --- |
| Login | Valid user reaches inventory; invalid password shows an error |
| Products | Sort products by ascending price |
| Cart | Added backpack appears in cart |
| Checkout | Cart opens the customer information step |
| Logout | Logged-in user returns to login |

The tests use SauceDemo's public demo credentials: `standard_user` / `secret_sauce`. Checkout stops at the customer information form; no order or payment is submitted.

## Requirements

- JDK 17
- Maven 3.9+
- Internet access to download the Playwright Chromium browser and access SauceDemo

## Install the browser

Run once after cloning:

```bash
mvn exec:java -Dexec.args="install chromium"
```

On Linux CI, install Chromium and its OS dependencies with:

```bash
mvn exec:java -Dexec.args="install --with-deps chromium"
```

## Run the tests

Visible Chromium (default):

```bash
mvn clean test
```

Headless Chromium:

```bash
mvn clean test -Dplaywright.headless=true
```

Override the demo application URL:

```bash
mvn clean test -Dsite.url=https://www.saucedemo.com/
```

TestNG reports and failure screenshots are generated under `target/`.

## Project structure

```text
src/
├── main/java/com/saucedemo/playwright/
│   ├── base/                 # Shared Page Object base
│   └── pages/                # Login, products, cart, checkout page objects
└── test/java/com/saucedemo/playwright/
    ├── base/                 # Playwright lifecycle per TestNG test
    ├── testdata/             # Public demo credentials
    └── tests/                # Focused scenarios by feature
```

## Selenium comparison

The sibling `Priyanka_Selenium-main` project targets the same SauceDemo site and equivalent workflows. In Playwright, a `BrowserContext` gives each test an isolated session, and `Locator` actions auto-wait for elements to be actionable; Selenium uses `WebDriver`, explicit waits, and browser driver management.

## CI

GitHub Actions installs Playwright Chromium with its Linux dependencies, runs the TestNG suite headlessly, and uploads reports and failure screenshots.
