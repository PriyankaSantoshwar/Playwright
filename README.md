# SauceDemo UI Automation - Playwright Java

A standalone Playwright for Java + TestNG portfolio project automating realistic e-commerce journeys on [SauceDemo](https://www.saucedemo.com), a public application designed for software testing practice.

This project is intentionally separate from the Selenium project. Both automate the same website and feature flows, making the two repositories useful for comparing WebDriver-based Selenium with Playwright's browser, context, page, locator, and auto-waiting model.

## What it demonstrates

- Playwright Java Page Object Model
- Locator-based interactions with built-in auto-waiting
- Isolated browser context and page per TestNG test
- Positive and negative login coverage
- Product sorting, cart validation, checkout form, and logout flows
- Chromium and Firefox browser coverage
- Failure screenshots saved under `target/screenshots`
- Headless CI execution through GitHub Actions
- API testing with Playwright's `APIRequestContext`

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
| API | Retrieve and filter posts, retrieve related comments, create/update/delete posts, and verify a not-found response with JSONPlaceholder |

The tests use SauceDemo's public demo credentials: `standard_user` / `secret_sauce`. Checkout stops at the customer information form; no order or payment is submitted.
SauceDemo does not provide a documented public API for this project, so the API examples use the public JSONPlaceholder practice API. The API tests use Playwright's built-in API client and do not launch a browser.

## Requirements

- JDK 17
- Maven 3.9+
- Internet access to download the Playwright Chromium browser and access SauceDemo and JSONPlaceholder

## Install the browser

Install Chromium (default) or Firefox:

```bash
mvn exec:java -Dexec.args="install chromium"
mvn exec:java -Dexec.args="install firefox"
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

Run the suite in Firefox:

```bash
mvn clean test -Dplaywright.headless=true -Dplaywright.browser=firefox
```

The `playwright.browser` property accepts `chromium` (default) or `firefox`. GitHub Actions runs the suite against both browsers.

Override the demo application URL:

```bash
mvn clean test -Dsite.url=https://www.saucedemo.com/
```

Override the API base URL:

```bash
mvn clean test -Dapi.base.url=https://jsonplaceholder.typicode.com
```

TestNG reports and failure screenshots are generated under `target/`.

### Run tests and email the report locally

The local runner executes the headless TestNG suite, then emails a summary with the Surefire XML reports attached as a ZIP file. It uses Python's standard library; Maven and the Playwright Chromium browser must be installed first.

Set the same SMTP and recipient environment variables documented in the CI section (`SMTP_SERVER`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `REPORT_EMAIL_TO`, and `REPORT_EMAIL_FROM`). `SMTP_PORT` is optional and defaults to `587`; port `465` uses implicit TLS.

```bash
export SMTP_SERVER=smtp.example.com
export SMTP_PORT=587
export SMTP_USERNAME=your-smtp-username
export SMTP_PASSWORD=your-smtp-app-password
export REPORT_EMAIL_TO=recipient@example.com
export REPORT_EMAIL_FROM=sender@example.com
python3 scripts/run-tests-with-email.py
```

The script exits with the Maven test status. Email and SMTP configuration errors are printed explicitly; keep credentials in environment variables rather than source files.

## Project structure

```text
src/
├── main/java/com/saucedemo/playwright/
│   ├── base/                 # Shared Page Object base
│   └── pages/                # Login, products, cart, checkout page objects
└── test/java/com/saucedemo/playwright/
    ├── base/                 # Separate browser UI and API request lifecycles
    ├── testdata/             # Public demo credentials
    └── tests/                # Focused UI and API scenarios
```

## Selenium comparison

The sibling `Priyanka_Selenium-main` project targets the same SauceDemo site and equivalent workflows. In Playwright, a `BrowserContext` gives each test an isolated session, and `Locator` actions auto-wait for elements to be actionable; Selenium uses `WebDriver`, explicit waits, and browser driver management.

## CI

GitHub Actions runs on pushes and pull requests, and can also be started manually from the repository's **Actions** tab by selecting **Playwright UI tests**, choosing `qa` or `prod`, and clicking **Run workflow**. The workflow runs the suite headlessly in Chromium and Firefox, then uploads reports and failure screenshots. When email is configured, the workflow also emails a test summary and attaches the Surefire reports as a ZIP file.

Configure the target URLs as repository Actions variables under **Settings → Secrets and variables → Actions → Variables**:

| Variable | Description |
| --- | --- |
| `QA_SITE_URL` | QA site URL (optional; defaults to the public SauceDemo demo site) |
| `PROD_SITE_URL` | Production site URL (required to select `prod`) |

Push and pull request runs use `QA_SITE_URL`, or the public SauceDemo demo site when it is not set. Production runs fail clearly if `PROD_SITE_URL` is not configured.
Only select `prod` for an application you are authorized to test and where the configured test account is safe to use.

To enable email notifications, add these repository actions secrets:

| Secret | Description |
| --- | --- |
| `SMTP_SERVER` | SMTP server hostname |
| `SMTP_PORT` | SMTP server port (optional; defaults to `587`) |
| `SMTP_USERNAME` | SMTP username |
| `SMTP_PASSWORD` | SMTP password or app password |
| `REPORT_EMAIL_TO` | Report recipient address |
| `REPORT_EMAIL_FROM` | Sender address accepted by the SMTP server |

If the required secrets are not set, the workflow skips email and still uploads the test reports.
